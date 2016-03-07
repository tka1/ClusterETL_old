using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Net;
using System.Net.Sockets;
using System.Text.RegularExpressions;
using Npgsql;
using System.IO;

namespace ClusterEtl
{



    public class Etl
    {
        public static void Log(string logMessage, TextWriter w)
        {
            w.Write("\r\nLog Entry : ");
            w.WriteLine("{0} {1}", DateTime.Now.ToLongTimeString(),
                DateTime.Now.ToLongDateString());
            w.WriteLine("  :");
            w.WriteLine("  {0}", logMessage);
            w.WriteLine("-------------------------------");
        }
        public static void StartClient()
        {

            using (StreamWriter w = File.AppendText("log.txt"))
            {
                Log("ETL started", w);

            }

            while (true)
            {
                bool ConnStatus = false;
                string clusteradd = System.Configuration.ConfigurationManager.AppSettings["clusteradd"];
                string clusterport = System.Configuration.ConfigurationManager.AppSettings["clusterport"];
                int port = Int32.Parse(clusterport);
                Console.WriteLine("Trying connect");
                string dbserver = System.Configuration.ConfigurationManager.AppSettings["dbserver"];
                string database = System.Configuration.ConfigurationManager.AppSettings["database"];
                string userid = System.Configuration.ConfigurationManager.AppSettings["userid"];
                string password = System.Configuration.ConfigurationManager.AppSettings["password"];
                string skimmer_name = System.Configuration.ConfigurationManager.AppSettings["skimmer_name"];
                NpgsqlConnection conn = new NpgsqlConnection("Server=" + dbserver + ";User Id=" + userid + ";Password=Saturnus1!" + ";Database=" + database + ";");
                // Data buffer for incoming data.
                byte[] bytes = new byte[8028];
                // Connect to a remote device.
                try
                {
                    // Establish the remote endpoint for the socket.
                    IPHostEntry ipHostEntry = Dns.GetHostEntry(clusteradd);
                    IPAddress ipAddress = ipHostEntry.AddressList[1];
                    //IPEndPoint remoteEP = new IPEndPoint(IPAddress.Parse("127.0.0.1"), clusterport);
                    IPEndPoint remoteEP = new IPEndPoint(ipAddress, port);
                    // Create a TCP/IP  socket.
                    Socket sender = new Socket(AddressFamily.InterNetwork,
                    SocketType.Stream, ProtocolType.Tcp);
                    // Connect the socket to the remote endpoint. Catch any errors.
                    try
                    {
                        sender.Connect(remoteEP);
                        Console.WriteLine("Socket connected to {0}",
                          sender.RemoteEndPoint.ToString());
                         ConnStatus = true;
                        using (StreamWriter w = File.AppendText("log.txt"))
                        {
                            Log("Connected", w);

                        }
                        //send callsign
                        string call1 = "oh2bbt" + Environment.NewLine;
                        byte[] msg = Encoding.ASCII.GetBytes(call1);
                        int bytesSent = sender.Send(msg);
                        // Receive the response from the remote device.
                        // Encode the data string into a byte array.
                        // int looppi = 0;
                        // while (looppi <1000000)
                        while (true)
                        {
                            sender.ReceiveTimeout = 90000;
                            int bytesRec = sender.Receive(bytes);
                            string response = Encoding.ASCII.GetString(bytes, 0, bytesRec);
                            response = response.Trim();
                            if (response.StartsWith("DX"))
                            {
                                string skimmode = "";
                                string mode = "";
                                string band = "";
                                //split respone to each line 
                                string[] lines = Regex.Split(response, "\r\n");
                                foreach (string line in lines)
                                {
                                    Console.WriteLine(line);
                                    string decallin = line.Substring(5, (line.IndexOf(":") - 7)).Trim();
                                    string dxcall = line.Substring((line.IndexOf(":") + 12), 10).Trim();
                                    string freq_orig = line.Substring((line.IndexOf(":") + 2), 9).Trim();
                                    string info = line.Substring((line.IndexOf(":") + 26), 30);
                                    info = info.Trim();
                                    //string mode = info.Substring((line.IndexOf("db") + 26), 30);
                                    // Console.WriteLine(info);
                                    string db = "";
                                    if (info.IndexOf("dB") > 2)
                                    {
                                         db = info.Substring((info.IndexOf("dB") - 3), 2);
                                    }
                                    else db = info.Substring((info.IndexOf("dB") - 2), 2);
                                    // Console.WriteLine(info.IndexOf("dB"));


                                    Match cqmodematch = Regex.Match(info, ("(.*)CQ(.*)"));
                                    if (cqmodematch.Success)
                                    {
                                        skimmode = "CQ";
                                    }
                                    Match demodematch = Regex.Match(info, ("(.*)DE(.*)"));
                                    if (demodematch.Success)
                                    {
                                        skimmode = "DE";
                                    }

                                    Match cwmatch = Regex.Match(info, ("(.*)CW(.*)"));
                                    if (cwmatch.Success)
                                    {
                                        mode = "CW";
                                    }
                                    Match rttymatch = Regex.Match(info, ("(.*)RTTY(.*)"));
                                    if (rttymatch.Success)
                                    {
                                        mode = "RTTY";
                                    }
                                    Match psk31match = Regex.Match(info, ("(.*)PSK31(.*)"));
                                    if (psk31match.Success)
                                    {
                                        mode = "PSK31";
                                    }
                                    Match psk63match = Regex.Match(info, ("(.*)PSK63(.*)"));
                                    if (psk63match.Success)
                                    {
                                        mode = "PSK63";
                                    }
                                    Match cwmatch2 = Regex.Match(info, ("(.*)WPM(.*)"));
                                    if (cwmatch2.Success)
                                    {
                                        mode = "CW";
                                    }

                                    freq_orig = freq_orig.Replace(".", ",");
                                    decimal freq = 0;
                                    decimal signal = 0;
                                    try
                                    {
                                        freq = Decimal.Parse(freq_orig);
                                        signal = Decimal.Parse(db);
                                    }
                                    catch (FormatException)
                                    {
                                        Console.WriteLine("format exception '{0}.", freq_orig);
                                        using (StreamWriter w = File.AppendText("log.txt"))
                                        {
                                            Log("format exception" + freq_orig, w);
                                        }
                                    }
                                    //Console.WriteLine(mode + ":");
                                    if (freq >= 1800 && freq <= 1900)
                                    {
                                        band = "160M";
                                    }
                                    if (freq >= 3500 && freq <= 3700)
                                    {
                                        band = "80M";
                                    }

                                    if (freq >= 5300 && freq <= 5500)
                                    {
                                        band = "60M";
                                    }

                                    if (freq >= 7000 && freq <= 7200)
                                    {
                                        band = "40M";
                                    }

                                    if (freq >= 10100 && freq <= 10150)
                                    {
                                        band = "30M";
                                    }

                                    if (freq >= 14000 && freq <= 14500)
                                    {
                                        band = "20M";
                                    }

                                    if (freq >= 18000 && freq <= 19000)
                                    {
                                        band = "17M";
                                    }

                                    if (freq >= 21000 && freq <= 21500)
                                    {
                                        band = "15M";
                                    }

                                    if (freq >= 24890 && freq <= 24990)
                                    {
                                        band = "12M";
                                    }

                                    if (freq >= 28000 && freq <= 28500)
                                    {
                                        band = "10M";
                                    }

                                    if (freq >= 50000 && freq <= 51500)
                                    {
                                        band = "6M";
                                    }

                                    if (freq >= 144000 && freq <= 146000)
                                    {
                                        band = "2M";
                                    }
                                    try
                                    {
                                        conn.Open();
                                        NpgsqlCommand cmd2 = new NpgsqlCommand("SELECT  country,continent,length(prefix) FROM cluster.dxcc where :value1 like concat(dxcc.prefix, '%') order by 3 desc limit 1", conn);
                                        cmd2.Parameters.Add(new NpgsqlParameter("value1", NpgsqlTypes.NpgsqlDbType.Text));
                                        cmd2.Parameters[0].Value = dxcall;
                                        // string dxcountry = (String)cmd2.ExecuteScalar();
                                        NpgsqlDataReader dr2 = cmd2.ExecuteReader();
                                        string dxcountry = "";
                                        string dx_cont = "";
                                        while (dr2.Read())
                                        {
                                            dxcountry = dr2[0].ToString();
                                            dx_cont = dr2[1].ToString();
                                        }
                                        conn.Close();
                                        conn.Open();



                                        NpgsqlCommand cmd3 = new NpgsqlCommand("SELECT  country,continent,length(prefix) FROM cluster.dxcc where :value1 like concat(dxcc.prefix, '%') order by 3 desc limit 1", conn);
                                        cmd3.Parameters.Add(new NpgsqlParameter("value1", NpgsqlTypes.NpgsqlDbType.Text));
                                        cmd3.Parameters[0].Value = decallin;
                                        // string decountry = (String)cmd3.ExecuteScalar();
                                        NpgsqlDataReader dr3 = cmd3.ExecuteReader();
                                        string decountry = "";
                                        string de_cont = "";
                                        while (dr3.Read())
                                        {
                                            decountry = dr3[0].ToString();
                                            de_cont = dr3[1].ToString();
                                        }
                                        conn.Close();
                                        conn.Open();

                                        NpgsqlCommand cmd = new NpgsqlCommand("insert into cluster.clustertable(dxcall,country,de_country,freq,decall,skimmode,mode,band,sig_noise,dx_continent,de_continent,title) values ( :value1 ,:value2,:value3,:value4,:value5,:value6,:value7,:value8,:value9,:value10,:value11,:value12)", conn);
                                        cmd.Parameters.Add(new NpgsqlParameter("value1", NpgsqlTypes.NpgsqlDbType.Text));
                                        cmd.Parameters.Add(new NpgsqlParameter("value2", NpgsqlTypes.NpgsqlDbType.Text));
                                        cmd.Parameters.Add(new NpgsqlParameter("value3", NpgsqlTypes.NpgsqlDbType.Text));
                                        cmd.Parameters.Add(new NpgsqlParameter("value4", NpgsqlTypes.NpgsqlDbType.Numeric));
                                        cmd.Parameters.Add(new NpgsqlParameter("value5", NpgsqlTypes.NpgsqlDbType.Text));
                                        cmd.Parameters.Add(new NpgsqlParameter("value6", NpgsqlTypes.NpgsqlDbType.Text));
                                        cmd.Parameters.Add(new NpgsqlParameter("value7", NpgsqlTypes.NpgsqlDbType.Text));
                                        cmd.Parameters.Add(new NpgsqlParameter("value8", NpgsqlTypes.NpgsqlDbType.Text));
                                        cmd.Parameters.Add(new NpgsqlParameter("value9", NpgsqlTypes.NpgsqlDbType.Numeric));
                                        cmd.Parameters.Add(new NpgsqlParameter("value10", NpgsqlTypes.NpgsqlDbType.Text));
                                        cmd.Parameters.Add(new NpgsqlParameter("value11", NpgsqlTypes.NpgsqlDbType.Text));
                                        cmd.Parameters.Add(new NpgsqlParameter("value12", NpgsqlTypes.NpgsqlDbType.Text));
                                        cmd.Parameters[0].Value = dxcall;
                                        cmd.Parameters[1].Value = dxcountry;
                                        cmd.Parameters[2].Value = decountry;
                                        cmd.Parameters[3].Value = freq;
                                        cmd.Parameters[4].Value = decallin;
                                        cmd.Parameters[5].Value = skimmode;
                                        cmd.Parameters[6].Value = mode;
                                        cmd.Parameters[7].Value = band;
                                        cmd.Parameters[8].Value = signal;
                                        cmd.Parameters[9].Value = dx_cont;
                                        cmd.Parameters[10].Value = de_cont;
                                        cmd.Parameters[11].Value = skimmer_name;
                                        NpgsqlDataReader dr = cmd.ExecuteReader();
                                        conn.Close();
                                    }
                                    catch (Exception ex)
                                    {
                                        System.Console.WriteLine("READING:");
                                        System.Console.WriteLine("  ERROR:" + ex.Message);
                                        using (StreamWriter w = File.AppendText("log.txt"))
                                        {
                                            Log(ex.Message, w);

                                        }
                                    }

                                }

                            }

                        }

                        // Release the socket.
                       // sender.Shutdown(SocketShutdown.Both);
                        //sender.Close();
                       // Console.WriteLine("ended");
                        // Console.ReadLine();
                    }
                    catch (ArgumentNullException ane)
                    {
                        Console.WriteLine("ArgumentNullException : {0}", ane.ToString());
                    }
                    catch (SocketException se)
                    {
                        
                        // Console.ReadLine();
                        //Console.WriteLine(ConnStatus);
                        if (ConnStatus)
                        {
                            Console.WriteLine("SocketException : {0}", se.ToString());
                            using (StreamWriter w = File.AppendText("log.txt"))
                            {

                                Log(se.ToString(), w);

                            }
                            // Release the socket.
                             sender.Shutdown(SocketShutdown.Both);
                            sender.Close();
                           ConnStatus = false;
                        }

                    }
                    catch (Exception e)
                    {
                        Console.WriteLine("Unexpected exception : {0}", e.ToString());
                        using (StreamWriter w = File.AppendText("log.txt"))
                        {
                            Log(e.ToString(), w);

                        }
                    }

                }
                catch (Exception e)
                {
                    Console.WriteLine(e.ToString());
                }
             
            }
        }

        public static int Main(String[] args)
        {
            StartClient();
            return 0;

        }
    }
}

