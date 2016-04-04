using System;
using System.IO;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Text.RegularExpressions;
using Npgsql;

namespace AdifReader
{
    class Program
    {
        static void Main(string[] args)
        {
            string dbserver = System.Configuration.ConfigurationManager.AppSettings["dbserver"];
            string database = System.Configuration.ConfigurationManager.AppSettings["database"];
            string userid = System.Configuration.ConfigurationManager.AppSettings["userid"];
            string password = System.Configuration.ConfigurationManager.AppSettings["password"];
            NpgsqlConnection conn = new NpgsqlConnection("Server=" + dbserver + ";User Id=" + userid + ";Password=xxxxxxxx" + ";Database=" + database + ";");
            try
            {
                // Create an instance of StreamReader to read from a file.
                // The using statement also closes the StreamReader.
                using (StreamReader sr = new StreamReader("OH2BBT.adi"))
                {
                    string line;

                    // Read and display lines from the file until 
                    // the end of the file is reached. 
                    string callsign = "";
                    string mode = "";
                    string band = "";
                    while ((line = sr.ReadLine()) != null)
                    {
                        
                        string[] x = Regex.Split(line.Replace("\n", "").Replace("\r", ""), @"<(.*?):.*?>([^<\t\n\r\f\v]+)").Where(S => !string.IsNullOrEmpty(S)).ToArray();

                        int callid = Array.IndexOf(x, "CALL");
                        if (callid != -1)
                        {
                             callsign = x[callid + 1];
                            Console.WriteLine(callsign.Trim());
                        }

                        int bandid = Array.IndexOf(x, "BAND");
                        if (callid != -1) {
                             band = x[bandid + 1];
                            Console.WriteLine(band.Trim());
                        }

                        int modeid = Array.IndexOf(x, "MODE");
                        if (callid != -1)
                        {
                             mode = x[modeid + 1];
                            Console.WriteLine(mode.Trim());
                        }

                        conn.Open();
                        NpgsqlCommand cmd2 = new NpgsqlCommand("SELECT  country,continent,length(prefix) FROM cluster.dxcc where :value1 like concat(dxcc.prefix, '%') order by 3 desc limit 1", conn);
                        cmd2.Parameters.Add(new NpgsqlParameter("value1", NpgsqlTypes.NpgsqlDbType.Text));
                        cmd2.Parameters[0].Value = callsign;
                        // string dxcountry = (String)cmd2.ExecuteScalar();
                        NpgsqlDataReader dr2 = cmd2.ExecuteReader();
                        string dxcountry = "";
                        //string dx_cont = "";
                        while (dr2.Read())
                        {
                            dxcountry = dr2[0].ToString();
                           // dx_cont = dr2[1].ToString();
                        }
                        conn.Close();
                        Console.WriteLine(dxcountry);
                        conn.Open();

                        NpgsqlCommand cmd = new NpgsqlCommand("insert into cluster.log(call,country,mode,band) values ( :value1 ,:value2,:value3,:value4)", conn);
                        cmd.Parameters.Add(new NpgsqlParameter("value1", NpgsqlTypes.NpgsqlDbType.Text));
                        cmd.Parameters.Add(new NpgsqlParameter("value2", NpgsqlTypes.NpgsqlDbType.Text));
                        cmd.Parameters.Add(new NpgsqlParameter("value3", NpgsqlTypes.NpgsqlDbType.Text));
                        cmd.Parameters.Add(new NpgsqlParameter("value4", NpgsqlTypes.NpgsqlDbType.Text));

                        cmd.Parameters[0].Value = callsign.Trim();
                        cmd.Parameters[1].Value = dxcountry.Trim();
                        cmd.Parameters[2].Value = mode.Trim();
                        cmd.Parameters[3].Value = band.Trim();

                        NpgsqlDataReader dr = cmd.ExecuteReader();
                        conn.Close();
                    }
                }
            }
            catch (Exception e)
            {

                // Let the user know what went wrong.
                Console.WriteLine("The file could not be read:");
                Console.WriteLine(e.Message);
            }

            Console.ReadKey();
        }







    


        }

    }

