using noteapi.Models;
using System;
using System.Collections;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
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
        public IEnumerable Gettextnote(int id)
        {
            List<note> l = db.notes.Where(x => x.userid == id).ToList();
                return l;
        }

        [HttpPost]
        public IHttpActionResult Posttextnote([FromBody]note textnote)
        {
            db.notes.Add(textnote);
            db.SaveChanges();
            return Ok(new { result = "success" });
        }
    }
}
