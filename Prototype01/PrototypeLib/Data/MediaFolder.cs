using System.Collections.Generic;
using System.ComponentModel.DataAnnotations;

namespace PrototypeLib.Data
{
    public class MediaFolder
    {
        public int Id { get; set; }

        [Required]
        [StringLength(255)]
        public string Name { get; set; }

        [Required]
        public string Path { get; set; }

        public virtual ICollection<MediaFile> Files { get; set; }
    }
}
