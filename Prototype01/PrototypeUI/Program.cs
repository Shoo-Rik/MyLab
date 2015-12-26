using PrototypeLib.Providers;
using System;
using System.Windows.Forms;

namespace PrototypeUI
{
    static class Program
    {
        /// <summary>
        /// The main entry point for the application.
        /// </summary>
        [STAThread]
        static void Main()
        {
            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            // TODO
            Application.Run(new MainForm(new IndexingProvider(new StorageProvider(2500), new HashValueProvider(1024))));
        }
    }
}
