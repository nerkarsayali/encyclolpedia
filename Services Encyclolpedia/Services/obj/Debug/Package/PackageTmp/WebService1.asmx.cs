using System;
using System.Collections.Generic;
using System.Linq;
using System.Web;
using System.Web.Services;

namespace Thinkdroid
{
    [WebService(Namespace = "http://tempuri.org/")]
    [WebServiceBinding(ConformsTo = WsiProfiles.BasicProfile1_1)]
    [System.ComponentModel.ToolboxItem(false)]
    // Pour autoriser l'appel de ce service Web depuis un script à l'aide d'ASP.NET AJAX, supprimez les marques de commentaire de la ligne suivante. 
    // [System.Web.Script.Services.ScriptService]
    public class WebService1 : System.Web.Services.WebService
    {
        String[] champions = new String[] {
        "Ahri",
        "Akali",
        "Alistar",
        "Amumu",
        "Anivia",
        "Annie",
        "Ashe",
        "Blitzcrank",
        "Brand",
        "Caitlyn",
        "Cassiopeia",
        "Cho'Gath",
        "Corki",
        "Dr. Mundo",
        "Evelynn",
        "Ezreal",
        "Fiddlesticks",
        "Fiora",
        "Fizz",
        "Galio",
        "Gangplank",
        "Garen",
        "Gragas",
        "Graves",
        "Heimerdinger",
        "Irelia",
        "Janna",
        "Jarvan IV",
        "Jax",
        "Karma",
        "Karthus",
        "Kassadin",
        "Katarina",
        "Kayle",
        "Kennen",
        "Kog'Maw",
        "LeBlanc",
        "Lee Sin",
        "Leona",
        "Lux",
        "Malphite",
        "Malzahar",
        "Maokai",
        "Master Yi",
        "Miss Fortune",
        "Mordekaiser",
        "Morgana",
        "Nasus",
        "Nautilus",
        "Nidalee",
        "Nocturne",
        "Nunu",
        "Olaf",
        "Orianna",
        "Pantheon",
        "Poppy",
        "Rammus",
        "Renekton",
        "Riven",
        "Rumble",
        "Ryze",
        "Sejuani",
        "Shaco",
        "Shen",
        "Shyvana",
        "Singed",
        "Sion",
        "Sivir",
        "Skarner",
        "Sona",
        "Soraka",
        "Swain",
        "Talon",
        "Taric",
        "Teemo",
        "Tristana",
        "Trundle",
        "Tryndamere",
        "Twisted Fate",
        "Twitch",
        "Udyr",
        "Urgot",
        "Vayne",
        "Veigar",
        "Viktor",
        "Vladimir",
        "Volibear",
        "Warwick",
        "Wukong",
        "Xerath",
        "Xin Zhao",
        "Yorick",
        "Ziggs",
        "Zilean",
        };

        [WebMethod]
        public int GetChampionRotationWeek()
        {
            return 16;
        }

        [WebMethod]
        public String[] GetChampionsList()
        {
            return champions;
        }

        [WebMethod]
        public String[] GetChampionRequests(String championName)
        {
            //TODO
            int revision = GetChampionRevision(championName);
            String[] requests = new String[1];

            requests[0] = "INSERT INTO Champions(\"Name\", \"Version\") VALUES (\"" + championName + "\", " + revision + ");";

            return requests;
        }

        [WebMethod]
        public int GetChampionRevision(String championName)
        {
            //TODO
            return 1;
        }

        [WebMethod]
        public int GetAnswerToUniverse()
        {
            return 42;
        }
    }
}
