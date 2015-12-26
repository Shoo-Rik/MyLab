using System;
using System.ComponentModel.DataAnnotations;
namespace PrototypeLib.Data
{
    public class MediaFile
    {
        public int Id { get; set; }

        [Required]
        public int ContentHashValue { get; set; }

        [Required]
        public int Size { get; set; }

        [Required]
        [StringLength(255)]
        public string Name { get; set; }

        [Required]
        public string Path { get; set; }

//        public int FolderId { get; set; }

//        public virtual MediaFolder Folder { get; set; }
    }
}
