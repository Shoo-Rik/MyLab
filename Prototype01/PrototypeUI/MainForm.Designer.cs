using System.IO;
namespace PrototypeUI
{
    partial class MainForm
    {
        /// <summary>
        /// Required designer variable.
        /// </summary>
        private System.ComponentModel.IContainer components = null;

        /// <summary>
        /// Clean up any resources being used.
        /// </summary>
        /// <param name="disposing">true if managed resources should be disposed; otherwise, false.</param>
        protected override void Dispose(bool disposing)
        {
            if (disposing && (components != null))
            {
                components.Dispose();
            }
            base.Dispose(disposing);
        }

        #region Windows Form Designer generated code

        /// <summary>
        /// Required method for Designer support - do not modify
        /// the contents of this method with the code editor.
        /// </summary>
        private void InitializeComponent()
        {
            this.components = new System.ComponentModel.Container();
            this.folderBrowserDialog1 = new System.Windows.Forms.FolderBrowserDialog();
            this.textBox1 = new System.Windows.Forms.TextBox();
            this.chooseFolderButton = new System.Windows.Forms.Button();
            this.startIndexingButton = new System.Windows.Forms.Button();
            this.exitButton = new System.Windows.Forms.Button();
            this.label1 = new System.Windows.Forms.Label();
            this.statusListBox = new System.Windows.Forms.ListBox();
            this.progressBar1 = new System.Windows.Forms.ProgressBar();
            this.timer1 = new System.Windows.Forms.Timer(this.components);
            this.searchPatternComboBox = new System.Windows.Forms.ComboBox();
            this.label2 = new System.Windows.Forms.Label();
            this.label3 = new System.Windows.Forms.Label();
            this.searchOptionComboBox = new System.Windows.Forms.ComboBox();
            this.SuspendLayout();
            // 
            // folderBrowserDialog1
            // 
            this.folderBrowserDialog1.RootFolder = System.Environment.SpecialFolder.MyComputer;
            // 
            // textBox1
            // 
            this.textBox1.Location = new System.Drawing.Point(15, 23);
            this.textBox1.Name = "textBox1";
            this.textBox1.Size = new System.Drawing.Size(364, 20);
            this.textBox1.TabIndex = 1;
            // 
            // chooseFolderButton
            // 
            this.chooseFolderButton.Location = new System.Drawing.Point(385, 20);
            this.chooseFolderButton.Name = "chooseFolderButton";
            this.chooseFolderButton.Size = new System.Drawing.Size(112, 23);
            this.chooseFolderButton.TabIndex = 2;
            this.chooseFolderButton.Text = "Choose Directory";
            this.chooseFolderButton.UseVisualStyleBackColor = true;
            this.chooseFolderButton.Click += new System.EventHandler(this.chooseFolderButton_Click);
            // 
            // startIndexingButton
            // 
            this.startIndexingButton.Location = new System.Drawing.Point(12, 199);
            this.startIndexingButton.Name = "startIndexingButton";
            this.startIndexingButton.Size = new System.Drawing.Size(112, 44);
            this.startIndexingButton.TabIndex = 3;
            this.startIndexingButton.Text = "Start Indexing";
            this.startIndexingButton.UseVisualStyleBackColor = true;
            this.startIndexingButton.Click += new System.EventHandler(this.startIndexingButton_Click);
            // 
            // exitButton
            // 
            this.exitButton.Location = new System.Drawing.Point(385, 199);
            this.exitButton.Name = "exitButton";
            this.exitButton.Size = new System.Drawing.Size(112, 44);
            this.exitButton.TabIndex = 4;
            this.exitButton.Text = "Exit";
            this.exitButton.UseVisualStyleBackColor = true;
            this.exitButton.Click += new System.EventHandler(this.exitButton_Click);
            // 
            // label1
            // 
            this.label1.AutoSize = true;
            this.label1.Location = new System.Drawing.Point(12, 7);
            this.label1.Name = "label1";
            this.label1.Size = new System.Drawing.Size(86, 13);
            this.label1.TabIndex = 5;
            this.label1.Text = "Source Directory";
            // 
            // statusListBox
            // 
            this.statusListBox.FormattingEnabled = true;
            this.statusListBox.Location = new System.Drawing.Point(15, 137);
            this.statusListBox.Name = "statusListBox";
            this.statusListBox.Size = new System.Drawing.Size(482, 56);
            this.statusListBox.TabIndex = 6;
            // 
            // progressBar1
            // 
            this.progressBar1.Location = new System.Drawing.Point(15, 99);
            this.progressBar1.Name = "progressBar1";
            this.progressBar1.Size = new System.Drawing.Size(482, 23);
            this.progressBar1.Step = 1;
            this.progressBar1.TabIndex = 7;
            // 
            // timer1
            // 
            this.timer1.Interval = 200;
            this.timer1.Tick += new System.EventHandler(this.timer1_Tick);
            // 
            // searchPatternComboBox
            // 
            this.searchPatternComboBox.FormattingEnabled = true;
            this.searchPatternComboBox.Items.AddRange(new object[] {
            "*.*",
            "*.jpg"});
            this.searchPatternComboBox.Location = new System.Drawing.Point(15, 62);
            this.searchPatternComboBox.Name = "searchPatternComboBox";
            this.searchPatternComboBox.Size = new System.Drawing.Size(118, 21);
            this.searchPatternComboBox.TabIndex = 8;
            // 
            // label2
            // 
            this.label2.AutoSize = true;
            this.label2.Location = new System.Drawing.Point(12, 46);
            this.label2.Name = "label2";
            this.label2.Size = new System.Drawing.Size(78, 13);
            this.label2.TabIndex = 9;
            this.label2.Text = "Search Pattern";
            // 
            // label3
            // 
            this.label3.AutoSize = true;
            this.label3.Location = new System.Drawing.Point(139, 46);
            this.label3.Name = "label3";
            this.label3.Size = new System.Drawing.Size(75, 13);
            this.label3.TabIndex = 11;
            this.label3.Text = "Search Option";
            // 
            // searchOptionComboBox
            // 
            this.searchOptionComboBox.DropDownStyle = System.Windows.Forms.ComboBoxStyle.DropDownList;
            this.searchOptionComboBox.FormattingEnabled = true;
            this.searchOptionComboBox.Items.AddRange(new object[] {
            System.IO.SearchOption.TopDirectoryOnly,
            System.IO.SearchOption.AllDirectories});
            this.searchOptionComboBox.Location = new System.Drawing.Point(139, 62);
            this.searchOptionComboBox.Name = "searchOptionComboBox";
            this.searchOptionComboBox.Size = new System.Drawing.Size(121, 21);
            this.searchOptionComboBox.TabIndex = 10;
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(508, 253);
            this.Controls.Add(this.label3);
            this.Controls.Add(this.searchOptionComboBox);
            this.Controls.Add(this.label2);
            this.Controls.Add(this.searchPatternComboBox);
            this.Controls.Add(this.progressBar1);
            this.Controls.Add(this.statusListBox);
            this.Controls.Add(this.label1);
            this.Controls.Add(this.exitButton);
            this.Controls.Add(this.startIndexingButton);
            this.Controls.Add(this.chooseFolderButton);
            this.Controls.Add(this.textBox1);
            this.Name = "Form1";
            this.StartPosition = System.Windows.Forms.FormStartPosition.CenterScreen;
            this.Text = "Media File Indexing";
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.FolderBrowserDialog folderBrowserDialog1;
        private System.Windows.Forms.TextBox textBox1;
        private System.Windows.Forms.Button chooseFolderButton;
        private System.Windows.Forms.Button startIndexingButton;
        private System.Windows.Forms.Button exitButton;
        private System.Windows.Forms.Label label1;
        private System.Windows.Forms.ListBox statusListBox;
        private System.Windows.Forms.ProgressBar progressBar1;
        private System.Windows.Forms.Timer timer1;
        private System.Windows.Forms.ComboBox searchPatternComboBox;
        private System.Windows.Forms.Label label2;
        private System.Windows.Forms.Label label3;
        private System.Windows.Forms.ComboBox searchOptionComboBox;
    }
}

