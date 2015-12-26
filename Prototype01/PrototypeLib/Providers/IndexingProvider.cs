using PrototypeLib.Data;
using PrototypeLib.Interfaces;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.IO;
using System.Text;

namespace PrototypeLib.Providers
{
    public class IndexingProvider : IIndexingProvider
    {
        private IStorageProvider _storageProvider;
        private IHashValueProvider _hashValueProvider;

        public IndexingProvider(IStorageProvider storageProvider, IHashValueProvider hashValueProvider)
        {
            _storageProvider = storageProvider;
            _hashValueProvider = hashValueProvider;
        }

        public int ProcessDirectory(string directoryPath, string searchPattern, SearchOption searchOption, RefreshCounter refreshCounter)
        {
            if (_storageProvider == null)
                return -2; // TODO

            if (_hashValueProvider == null)
                return -4; // TODO

            if (String.IsNullOrEmpty(directoryPath))
                return -3; // TODO

            return _storageProvider.PutMediaInfo(
                GetMediaFiles(directoryPath, searchPattern, searchOption), refreshCounter);
        }

        private IEnumerable<MediaFile> GetMediaFiles(string directoryPath, string searchPattern, SearchOption searchOption)
        {
            foreach (string filePath in Directory.GetFiles(directoryPath, searchPattern, searchOption))
            {
                FileInfo fi = new FileInfo(filePath);
                int hashValue = _hashValueProvider.CalculateFileHash(filePath);
                //string hashValueString = String.Format("0x{0}", String.Join("", BitConverter.ToString(hashValue).Split('-')));

                yield return new MediaFile
                {
                    Name = fi.Name,
                    Path = fi.FullName,
                    Size = (int)fi.Length, // Do not expect files with size larger than 4G
                    ContentHashValue = hashValue,
                };
            }
        }
    }
}
