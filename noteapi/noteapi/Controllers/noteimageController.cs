using noteapi.Models;
using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web;
using System.Web.Http;

namespace noteapi.Controllers
{
    public class noteimageController : ApiController
    {
        dbcontext db = new dbcontext();
        [HttpPost]
        public HttpResponseMessage UploadFiles()
        {
            try {
                var res = new HttpResponseMessage(HttpStatusCode.OK);
                var req = HttpContext.Current.Request;
                if (req.Files.Count > 0)
                {
                    var files = new List<string>();
                    note notemain = new note();
                    db.notes.Add(notemain);
                    db.SaveChanges();
                    foreach(string item in req.Files)
                    {
                        var postedfile = req.Files[item];
                        var filepath = HttpContext.Current.Server.MapPath("~/Images/" + postedfile.FileName);
                        postedfile.SaveAs(filepath);
                        files.Add(filepath);
                    }
                    return new HttpResponseMessage(HttpStatusCode.OK);
                }
                else
                {
                    return new HttpResponseMessage(HttpStatusCode.BadRequest);
                }
                
            }
            catch
            {
                return new HttpResponseMessage(HttpStatusCode.BadRequest);
            }
        }
        
    }
}
    

