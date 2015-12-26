using PrototypeLib.Data;
using System.Collections.Generic;

namespace PrototypeLib.Interfaces
{
    public interface IStorageProvider
    {
        int PutMediaInfo(IEnumerable<MediaFile> entities, RefreshCounter refreshCounter);
    }
}
