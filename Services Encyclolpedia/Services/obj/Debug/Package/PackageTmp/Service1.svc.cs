using System;
using System.Collections.Generic;
using System.Linq;
using System.Runtime.Serialization;
using System.ServiceModel;
using System.ServiceModel.Web;
using System.Text;

namespace Thinkdroid
{
    // REMARQUE : vous pouvez utiliser la commande Renommer du menu Refactoriser pour changer le nom de classe "LeagueOfLegends" dans le code, le fichier svc et le fichier de configuration.
    public class LeagueOfLegends : IService1
    {
        public int GetChampionRotationWeek()
        {
            return 33;
        }
    }
}
