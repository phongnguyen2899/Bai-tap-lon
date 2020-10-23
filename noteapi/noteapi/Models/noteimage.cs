namespace noteapi.Models
{
    using System;
    using System.Collections.Generic;
    using System.ComponentModel.DataAnnotations;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Data.Entity.Spatial;

    [Table("noteimage")]
    public partial class noteimage
    {
        public int id { get; set; }

        public int? noteid { get; set; }

        [StringLength(250)]
        public string img { get; set; }

        public virtual note note { get; set; }
    }
}
