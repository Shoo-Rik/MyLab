using PrototypeLib.Data;
using System.Collections.Generic;
using System.IO;

namespace PrototypeLib.Interfaces
{
    public delegate void RefreshCounter(int value);

    public interface IIndexingProvider
    {
        int ProcessDirectory(string directoryPath, string searchPattern, SearchOption searchOption, RefreshCounter refreshCounter);
    }
}
