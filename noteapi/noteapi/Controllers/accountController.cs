using Common;
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
    
    public class accountController : ApiController
    {
        dbcontext db = new dbcontext();
      
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
                    if (x.username == username && x.password == password)
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
       /* public IHttpActionResult Postaccount(account user)
        {
            if (user.username == "" || user.email == "" || user.password == "" )
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
        }*/
        [HttpPost]
        public IHttpActionResult Postaccount([FromBody]account user)
        {
            List<account> l = db.accounts.Where(x => x.username == user.username).ToList();
            if (l.Count > 0)
            {
                return Ok(new { result = "err" });
            }
            else
            {
                
                Random r = new Random();
                int codres = r.Next(1000, 9999);
                user.status = false;
                user.code = codres;
                db.accounts.Add(user);
                db.SaveChanges();
                new Mailhelper().SendMail(user.email, "ma active", codres.ToString());
                return Ok(new { result = "success",id=""+user.id+"" });
            }
        }

        [HttpGet]
        public IHttpActionResult Getactive(int id,int code)
        {
            account user = db.accounts.Where(x => x.id == id).First();
            if (user != null)
            {
                if (user.code == code)
                {
                    user.status = true;
                    db.SaveChanges();
                    return Ok(new { result = "success" });
                }
                else
                {
                    return Ok(new { result = "err" });
                }
            }
            else
            {
                return Ok(new { result = "err" });
            }
        }
        [HttpGet]
        [Route("{username}/{password}/{newpassword}")]
        public IHttpActionResult GetChangepwd(string username, string password, string newpassword) {
            account item = db.accounts.Where(x => x.username == username).ToList().FirstOrDefault();
            if (item==null)
            {
                return Ok(new { result = "notfound" });
            }
            else
            {
                if (item.password != password) {
                    return Ok(new { result = "errpwd" });
                }
                else
                {
                    item.password = newpassword;
                    db.SaveChanges();
                    return Ok(new { result = "success" });
                }
            }
        }

    }
}
