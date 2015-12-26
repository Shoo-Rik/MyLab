using PrototypeLib.Providers;
using System;
using System.Reflection;
using System.Runtime.InteropServices;
using System.Threading;
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
            Guid guid = Marshal.GetTypeLibGuidForAssembly(Assembly.GetExecutingAssembly());
            bool createdNew;
            Mutex mutex = new Mutex(true, guid.ToString(), out createdNew);
            if (!createdNew)
                return;

            Application.EnableVisualStyles();
            Application.SetCompatibleTextRenderingDefault(false);
            // TODO
            Application.Run(new MainForm(new IndexingProvider(new StorageProvider(2500), new HashValueProvider(1024))));
        }
    }
}
