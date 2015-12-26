using PrototypeLib.Interfaces;
using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.IO;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace PrototypeUI
{
    public partial class MainForm : Form
    {
        private IIndexingProvider _indexingProvider;

        private int _internalCounter = 0;
        private DateTime _internalStartTime;

        public MainForm()
        {
            InitializeComponent();
        }

        public MainForm(IIndexingProvider indexingProvider)
        {
            InitializeComponent();

            _indexingProvider = indexingProvider;

            searchPatternComboBox.SelectedIndex = 1;
            searchOptionComboBox.SelectedIndex = 1;
        }

        public void UpdateCounter(int value)
        {
            _internalCounter = value;
        }

        private void exitButton_Click(object sender, EventArgs e)
        {
            // TODO
            Application.Exit();
        }

        private void chooseFolderButton_Click(object sender, EventArgs e)
        {
            DialogResult res = folderBrowserDialog1.ShowDialog();
            if (res == System.Windows.Forms.DialogResult.OK)
            {
                textBox1.Text = folderBrowserDialog1.SelectedPath;
            }
        }

        private async void startIndexingButton_Click(object sender, EventArgs e)
        {
            PreProcessControl();

            try
            {
                string sourceFolder = textBox1.Text;
                if (String.IsNullOrEmpty(sourceFolder))
                {
                    MessageBox.Show("Empty path");
                    return;
                }
                string searchPattern = searchPatternComboBox.Text;
                SearchOption searchOption = (searchOptionComboBox.Text == "AllDirectories") ?
                    SearchOption.AllDirectories : SearchOption.TopDirectoryOnly;

                int count = await ProcessFilesAsync(sourceFolder, searchPattern, searchOption);
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
            finally
            {
                PostProcessControl();
            }
        }

        /*private void startIndexingButton_Click(object sender, EventArgs e)
        {
            PreProcessControl();

            try
            {
                string sourceFolder = textBox1.Text;
                if (String.IsNullOrEmpty(sourceFolder))
                {
                    MessageBox.Show("Empty path");
                    return;
                }
                string searchPattern = searchPatternComboBox.Text;
                SearchOption searchOption = (searchOptionComboBox.Text == "AllDirectories") ?
                    SearchOption.AllDirectories : SearchOption.TopDirectoryOnly;

                ProcessFilesInBackgroundWorker(sourceFolder, searchPattern, searchOption);
            }
            catch (Exception ex)
            {
                MessageBox.Show(ex.Message);
            }
            finally
            {
                PostProcessControl();
            }
        }*/

        private void PreProcessControl()
        {
            timer1.Enabled = true;
            progressBar1.Value = progressBar1.Minimum;

            textBox1.Enabled = false;
            searchPatternComboBox.Enabled = false;
            searchOptionComboBox.Enabled = false;
            chooseFolderButton.Enabled = false;
            startIndexingButton.Enabled = false;

            _internalCounter = 0;
            _internalStartTime = DateTime.Now;
        }

        private void PostProcessControl()
        {
            timer1.Enabled = false;
            progressBar1.Value = progressBar1.Maximum;

            textBox1.Enabled = true;
            searchPatternComboBox.Enabled = true;
            searchOptionComboBox.Enabled = true;
            chooseFolderButton.Enabled = true;
            startIndexingButton.Enabled = true;
        }

        private async Task<int> ProcessFilesAsync(string sourceFolder, string searchPattern, SearchOption searchOption)
        {
            if (_indexingProvider == null)
                return -1; // TODO

            return await Task.Run<int>(() => 
            {
                return _indexingProvider.ProcessDirectory(sourceFolder, searchPattern, searchOption, UpdateCounter); 
            });
        }

        /*private int ProcessFilesInBackgroundWorker(string sourceFolder, string searchPattern, SearchOption searchOption)
        {
            if (_indexingProvider == null)
                return -1; // TODO

            BackgroundWorker bw = new BackgroundWorker();
            bw.WorkerReportsProgress = true;

            bw.DoWork += (sender, e) =>
            {
                _indexingProvider.ProcessDirectory(sourceFolder, searchPattern, searchOption, UpdateCounter);
            };

            bw.RunWorkerCompleted += (sender, eventArgs) =>
            {
                // TODO
            };

            bw.RunWorkerAsync();

            return 0;
        }*/


        private void timer1_Tick(object sender, EventArgs e)
        {
            if (progressBar1.Value == progressBar1.Maximum)
                progressBar1.Value = progressBar1.Minimum;

            progressBar1.PerformStep();

            statusListBox.DataSource = new[] { 
                String.Format("{0}: {1} files processed...", 
                    (DateTime.Now - _internalStartTime).ToString(), _internalCounter) 
            };
        }
    }
}
