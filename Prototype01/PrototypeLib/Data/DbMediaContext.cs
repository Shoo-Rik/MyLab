using PrototypeLib.Providers;
using System.Data.Entity;

namespace PrototypeLib.Data
{
    public class DbMediaContext : DbContext
    {
        public DbMediaContext()
            : base("MediaDatabase")
        {
            // TODO
            Database.SetInitializer<DbMediaContext>(new DropCreateDatabaseAlwaysInitializer()); 
        }

        public DbSet<MediaFile> MediaFileSet { get; set; }
//        public DbSet<MediaFolder> MediaDirectorySet { get; set; }
    }
}
