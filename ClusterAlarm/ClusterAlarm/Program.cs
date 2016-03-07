using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using LinqToTwitter;
using Npgsql;


namespace ClusterAlarm
{
    class Program
    {
        static void Main(string[] args)
        {

            string dbserver = System.Configuration.ConfigurationManager.AppSettings["dbserver"];
            string database = System.Configuration.ConfigurationManager.AppSettings["database"];
            string userid = System.Configuration.ConfigurationManager.AppSettings["userid"];
            string password = System.Configuration.ConfigurationManager.AppSettings["password"];
            NpgsqlConnection conn = new NpgsqlConnection("Server=" + dbserver + ";User Id=" + userid + ";Password=Saturnus1!" + ";Database=" + database + ";");
            const string accessToken = "706060410919768064-CZeVKheTbkIPnuwcr0Qrt5wDy0SODWm";
            const string accessTokenSecret = "QDmECT5sUDxVUKdx557EfCsZYAMgIYCY8zuyLPyqH68fa";
            const string consumerKey = "3cH1GIp17dOnNQl9M3X9Mh4b9";
            const string consumerSecret = "8sY3HShekmRux4vvhC5gyqpwXDBehYup1Fsd8K53jBrDVZhOfB";
            const string twitterAccountToDisplay = "kalliotj";
            string title = "";
            string time = "";
            string call = "";
            string freq = "";
            string band = "";
            string country = "";
            string mode = "";

            var authorizer = new SingleUserAuthorizer
            {
                CredentialStore = new InMemoryCredentialStore
                {
                    ConsumerKey = consumerKey,
                    ConsumerSecret = consumerSecret,
                    OAuthToken = accessToken,
                    OAuthTokenSecret = accessTokenSecret
                }
            };
            while (true)
            {
                try

                {
                    conn.Open();
                    NpgsqlCommand cmd2 = new NpgsqlCommand("SELECT  distinct title,now(), dxcall, freq, band, country, mode, dx_continent,skimmode FROM cluster.alert", conn);
                    //cmd2.Parameters.Add(new NpgsqlParameter("value1", NpgsqlTypes.NpgsqlDbType.Text));
                    // cmd2.Parameters[0].Value = callsign;
                    // string dxcountry = (String)cmd2.ExecuteScalar();
                    NpgsqlDataReader dr2 = cmd2.ExecuteReader();

                    //string dx_cont = "";
                    while (dr2.Read())
                    {
                        title = dr2[0].ToString();
                        time = dr2[1].ToString();
                        call = dr2[2].ToString();
                        freq = dr2[3].ToString();
                        band = dr2[4].ToString();
                        country = dr2[5].ToString();
                        mode = dr2[6].ToString();
                        Console.WriteLine(title + " " + time + " " + call + " " + freq + " " + band + " " + mode + " " + country);
                        using (var context = new TwitterContext(authorizer))
                        {
                            if (call != "")
                            {

                                  var tweet = context.TweetAsync(title + " " + time + " " + call + " " + freq + " " + band + " " + mode + " " + country).Result;
                            }
                        };
                    }

                   
                    conn.Close();

                }
                catch (Exception e)
                {
                    Console.WriteLine(e);
                }
          
               /* try
                {
                   

                }
                catch (Exception e)
                { Console.WriteLine(e); }
                */
                //  Console.ReadLine();
                System.Threading.Thread.Sleep(59000);
            }

        }

    }
}
