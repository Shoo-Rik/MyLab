using PrototypeLib.Data;
using PrototypeLib.Interfaces;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;

namespace PrototypeLib.Providers
{
    public class StorageProvider : IStorageProvider
    {
        private int _changesBatchSize = 2500;

        public StorageProvider()
        {
        }

        public StorageProvider(int changesBatchSize)
        {
            if (changesBatchSize > 0)
            {
                _changesBatchSize = changesBatchSize;
            }
        }

        public int PutMediaInfo(IEnumerable<MediaFile> entities, RefreshCounter refreshCounter)
        {
            Stopwatch swPutMediaInfo = new Stopwatch();

            swPutMediaInfo.Start();

            int count = 0;
            List<MediaFile> batch = new List<MediaFile>(_changesBatchSize);

            using (DbMediaContext context = new DbMediaContext())
            {
                HashSet<int> hash = null;
                if (context.MediaFileSet.Any())
                {
                    var existedHashValues = from f in context.MediaFileSet select f.ContentHashValue;
                    hash = new HashSet<int>(existedHashValues.AsEnumerable());
                }

                foreach (MediaFile entity in entities)
                {
                    if (hash != null && hash.Contains(entity.ContentHashValue))
                    {
                        var d = from f in context.MediaFileSet
                                where (f.ContentHashValue == entity.ContentHashValue) && (f.Path == entity.Path)
                                select f;
                        if (d.Any())
                        {
                            continue;
                        }
                    }

                    batch.Add(entity);

                    refreshCounter(++count);
                    if (count % _changesBatchSize == 0)
                    {
                        context.MediaFileSet.AddRange(batch.ToArray());
                        context.SaveChanges();
                        batch.Clear();
                    }
                }
                if (batch.Count > 0)
                {
                    context.MediaFileSet.AddRange(batch.ToArray());
                    context.SaveChanges();
                    batch.Clear();
                }
            }

            swPutMediaInfo.Stop();

            return count;
        }

        /*private class MediaFileComparer : EqualityComparer<MediaFile>
        {
            private static MediaFileComparer _instance = new MediaFileComparer();

            public static MediaFileComparer Instance { get { return _instance; } }

            public override bool Equals(MediaFile x, MediaFile y)
            {
                if (x == null || y == null)
                    return (x == y);

                return (x.ContentHashValue == y.ContentHashValue) && String.Equals(x.Path, y.Path, StringComparison.OrdinalIgnoreCase);
            }

            public override int GetHashCode(MediaFile obj)
            {
                return (obj == null) ?
                    0 : ((obj.ContentHashValue == null) ? 
                        0 : obj.ContentHashValue.GetHashCode());
            }
        }*/
    }
}
