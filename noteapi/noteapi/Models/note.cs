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
        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2214:DoNotCallOverridableMethodsInConstructors")]
        public note()
        {
            noteimages = new HashSet<noteimage>();
        }

        public int id { get; set; }

        public int? userid { get; set; }

        [StringLength(250)]
        public string title { get; set; }

        public string content { get; set; }

        [StringLength(250)]
        public string gps { get; set; }

        public DateTime? createdate { get; set; }

        public string img { get; set; }

        public virtual account account { get; set; }

        [System.Diagnostics.CodeAnalysis.SuppressMessage("Microsoft.Usage", "CA2227:CollectionPropertiesShouldBeReadOnly")]
        public virtual ICollection<noteimage> noteimages { get; set; }
    }
}
