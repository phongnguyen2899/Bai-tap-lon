namespace noteapi.Models
{
    using System;
    using System.Data.Entity;
    using System.ComponentModel.DataAnnotations.Schema;
    using System.Linq;

    public partial class dbcontext : DbContext
    {
        public dbcontext()
            : base("name=dbcontext")
        {
        }

        public virtual DbSet<account> accounts { get; set; }
        public virtual DbSet<note> notes { get; set; }
        public virtual DbSet<sysdiagram> sysdiagrams { get; set; }

        protected override void OnModelCreating(DbModelBuilder modelBuilder)
        {
            modelBuilder.Entity<account>()
                .HasMany(e => e.notes)
                .WithRequired(e => e.account)
                .HasForeignKey(e => e.userid)
                .WillCascadeOnDelete(false);

            modelBuilder.Entity<note>()
                .Property(e => e.gps)
                .IsUnicode(false);
        }
    }
}
