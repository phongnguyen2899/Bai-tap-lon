using noteapi.Models;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Web.Http;

namespace noteapi.Controllers
{
    
    public class accountController : ApiController
    {
        dbcontext db = new dbcontext();

        public IHttpActionResult Getall()
        {
            return Ok(db.accounts.ToList());
        }
        [HttpGet]
        public IHttpActionResult GetResult(string username,string password)
        {
            if (username == "")
            {
                return Ok(new { result = "err" });
            }
            else
            {
                List<account> l = db.accounts.Where(item => item.username == username).ToList();
                
                if (l.Count<=0)
                {
                    return Ok(new { result = "err" });
                }
                else
                {
                    account x = l.First();
                    if (x.username == username && x.pasword == password)
                    {
                        return Ok(new { result = "ok",id=""+x.id+"" });
                    }
                    else
                    {
                        return Ok(new { result = "err" });
                    }
                }
            }
        }
        public IHttpActionResult Postaccount(account user)
        {
            if (user.username == "" || user.email == "" || user.pasword == "" || user.id == "")
            {
                return Ok(new { result = "err" });
            }
            else
            {
                List<account> l = db.accounts.Where(x => x.username == user.username).ToList();
                if (l.Count <= 0)
                {
                    return Ok(new { result = "err" });
                }
                else
                {

                    db.accounts.Add(user);
                    db.SaveChanges();
                    return Ok(new { result = "err" });
                }
            }
        }
    }
}
