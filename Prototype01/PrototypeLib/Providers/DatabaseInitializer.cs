using PrototypeLib.Data;
using System.Data.Entity;

namespace PrototypeLib.Providers
{
    public class DropCreateDatabaseAlwaysInitializer : DropCreateDatabaseAlways<DbMediaContext>
    {
        protected override void Seed(DbMediaContext context)
        {
            base.Seed(context);
        }
    }

    public class CreateDatabaseIfNotExistsInitializer : CreateDatabaseIfNotExists<DbMediaContext>
    {
        protected override void Seed(DbMediaContext context)
        {
            base.Seed(context);
        }
    }

}
