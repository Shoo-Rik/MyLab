using System;

namespace PrototypeLib.Interfaces
{
    public interface IHashValueProvider
    {
        int CalculateFileHash(string filePath);
    }
}
