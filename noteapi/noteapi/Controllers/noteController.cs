using noteapi.Models;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web;
using System.Web.Http;

namespace noteapi.Controllers
{
    public class noteController : ApiController
    {
        dbcontext db = new dbcontext();

        [HttpGet]
        public IEnumerable Getall()
        {
            List<noteimage> l = db.noteimages.ToList();
            return l;
        }

        [HttpGet]
        [Route("getnotebyuserid/{id}")]
        public IEnumerable Gettextnote(int id)
        {
            List<note> l = db.notes.Where(x => x.userid == id).ToList();
            return l;
        }


        [HttpPost]
        [Route("uploadtext")]
        public IHttpActionResult Posttextnote([FromBody]note textnote)
        {
            textnote.img = null;
            db.notes.Add(textnote);
            db.SaveChanges();
            return Ok(new { result = "success" });

        }


        [HttpPost]
        [Route("uploadimg")]
        public IHttpActionResult Posttextnote1()
        {
            try
            {
                var res = new HttpResponseMessage(HttpStatusCode.OK);
                var req = HttpContext.Current.Request;
                string filename = "";
                if (req.Files.Count > 0)
                {
                    var files = new List<string>();
                    foreach (string item in req.Files)
                    {
                        var postedfile = req.Files[item];
                        var filepath = HttpContext.Current.Server.MapPath("~/Images/" + postedfile.FileName);
                        postedfile.SaveAs(filepath);
                        files.Add(filepath);
                        filename += postedfile.FileName;
                        filename += ",";
                    }
                    note note = new note();
                    note.title = req.Form.GetValues("title").First();
                    note.img = filename;
                    note.userid = Int32.Parse(req.Form.GetValues("id").First());
                    note.gps = req.Form.GetValues("gps").First();
                    DateTime now = DateTime.Now;
                    note.createdate = now;
                    db.notes.Add(note);
                    db.SaveChanges();
                    return Ok(new { result = "success" });
                }
                else
                {

                    return Ok(new { result = "err" });
                }

            }
            catch
            {
                return Ok(new { result = "err" });
            }

        }


        [HttpGet]
        [Route("getnotebyid/{idnote}")]
        public note Gettextnotebyid(int idnote)
        {
            return db.notes.Where(x => x.id == idnote).First();
        }



        [HttpGet]
        [Route("delete/{id}")]
        public IHttpActionResult Deletenote(int id)
        {
            note item = db.notes.Where(x => x.id == id).ToList().FirstOrDefault();
            db.notes.Remove(item);
            db.SaveChanges();
            return Ok(new { result = "success" });
        }

    }
}
