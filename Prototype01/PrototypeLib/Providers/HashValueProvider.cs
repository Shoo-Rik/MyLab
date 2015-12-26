using PrototypeLib.Interfaces;
using System;
using System.IO;

namespace PrototypeLib.Providers
{
    public class HashValueProvider : IHashValueProvider
    {
        private int _bufferLength = 1024;

        public HashValueProvider()
        {
        }

        public HashValueProvider(int bufferLength)
        {
            if (bufferLength > 0)
            {
                _bufferLength = bufferLength;
            }
        }

        //private static HashAlgorithm ha = HashAlgorithm.Create("MD5");

        public int CalculateFileHash(string filePath)
        {
            using (FileStream fs = File.OpenRead(filePath))
            {
                byte[] buffer = new byte[_bufferLength];
                fs.Read(buffer, 0, _bufferLength);
                //return ha.ComputeHash(buffer);
                return GetHashCode(buffer);
            }
        }

        private static int GetHashCode(byte[] array)
        {
            var str = Convert.ToBase64String(array);
            return str.GetHashCode();
        }
    }
}
