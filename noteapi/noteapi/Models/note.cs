namespace noteapi.Models
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    [Table("note")]
    public partial class note
    {
        [StringLength(50)]
        public string id { get; set; }

        [Required]
        [StringLength(50)]
        public string userid { get; set; }

        [StringLength(50)]
        public string title { get; set; }

        public string content { get; set; }

        [StringLength(250)]
        public string gps { get; set; }

        [StringLength(250)]
        public string image { get; set; }

        public DateTime? createdate { get; set; }

        public virtual account account { get; set; }
    }
}
