namespace AgCubio
{

    partial class Form1
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
            this.PlayerBox = new System.Windows.Forms.TextBox();
            this.ServerBox = new System.Windows.Forms.TextBox();
            this.PlayerLabel = new System.Windows.Forms.Label();
            this.ServerLabel = new System.Windows.Forms.Label();
            this.ConnectButton = new System.Windows.Forms.Button();
            this.FPSLabel = new System.Windows.Forms.Label();
            this.FPSValue = new System.Windows.Forms.Label();
            this.FoodLabel = new System.Windows.Forms.Label();
            this.FoodValue = new System.Windows.Forms.Label();
            this.MassLabel = new System.Windows.Forms.Label();
            this.MassValue = new System.Windows.Forms.Label();
            this.WidthLabel = new System.Windows.Forms.Label();
            this.WidthValue = new System.Windows.Forms.Label();
            this.DiedLabel = new System.Windows.Forms.Label();
            this.TitleLabel = new System.Windows.Forms.Label();
            this.MaxMassLabel = new System.Windows.Forms.Label();
            this.MaxMassValue = new System.Windows.Forms.Label();
            this.MaxWidthLabel = new System.Windows.Forms.Label();
            this.MaxWidthValue = new System.Windows.Forms.Label();
            this.KingLabel = new System.Windows.Forms.Label();
            this.KingValue = new System.Windows.Forms.Label();
            this.RetryButton = new System.Windows.Forms.Button();
            this.ConnectionLabel = new System.Windows.Forms.Label();
            this.SuspendLayout();
            // 
            // PlayerBox
            // 
            this.PlayerBox.Anchor = System.Windows.Forms.AnchorStyles.None;
            this.PlayerBox.Location = new System.Drawing.Point(144, 118);
            this.PlayerBox.Margin = new System.Windows.Forms.Padding(2);
            this.PlayerBox.MinimumSize = new System.Drawing.Size(166, 4);
            this.PlayerBox.Name = "PlayerBox";
            this.PlayerBox.Size = new System.Drawing.Size(166, 20);
            this.PlayerBox.TabIndex = 0;
            this.PlayerBox.KeyDown += new System.Windows.Forms.KeyEventHandler(this.PlayerBox_KeyDown);
            // 
            // ServerBox
            // 
            this.ServerBox.Anchor = System.Windows.Forms.AnchorStyles.None;
            this.ServerBox.Location = new System.Drawing.Point(144, 163);
            this.ServerBox.Margin = new System.Windows.Forms.Padding(2);
            this.ServerBox.MinimumSize = new System.Drawing.Size(166, 4);
            this.ServerBox.Name = "ServerBox";
            this.ServerBox.Size = new System.Drawing.Size(166, 20);
            this.ServerBox.TabIndex = 1;
            this.ServerBox.Text = "localhost";
            this.ServerBox.KeyDown += new System.Windows.Forms.KeyEventHandler(this.ServerBox_KeyDown);
            // 
            // PlayerLabel
            // 
            this.PlayerLabel.Anchor = System.Windows.Forms.AnchorStyles.None;
            this.PlayerLabel.AutoSize = true;
            this.PlayerLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 13F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.PlayerLabel.Location = new System.Drawing.Point(19, 116);
            this.PlayerLabel.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
            this.PlayerLabel.Name = "PlayerLabel";
            this.PlayerLabel.Size = new System.Drawing.Size(113, 22);
            this.PlayerLabel.TabIndex = 2;
            this.PlayerLabel.Text = "Player Name";
            // 
            // ServerLabel
            // 
            this.ServerLabel.Anchor = System.Windows.Forms.AnchorStyles.None;
            this.ServerLabel.AutoSize = true;
            this.ServerLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 13F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.ServerLabel.Location = new System.Drawing.Point(68, 163);
            this.ServerLabel.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
            this.ServerLabel.Name = "ServerLabel";
            this.ServerLabel.Size = new System.Drawing.Size(63, 22);
            this.ServerLabel.TabIndex = 3;
            this.ServerLabel.Text = "Server";
            // 
            // ConnectButton
            // 
            this.ConnectButton.Anchor = System.Windows.Forms.AnchorStyles.None;
            this.ConnectButton.Location = new System.Drawing.Point(144, 192);
            this.ConnectButton.Margin = new System.Windows.Forms.Padding(2);
            this.ConnectButton.MaximumSize = new System.Drawing.Size(167, 39);
            this.ConnectButton.MinimumSize = new System.Drawing.Size(167, 39);
            this.ConnectButton.Name = "ConnectButton";
            this.ConnectButton.Size = new System.Drawing.Size(167, 39);
            this.ConnectButton.TabIndex = 4;
            this.ConnectButton.Text = "Connect";
            this.ConnectButton.UseVisualStyleBackColor = true;
            this.ConnectButton.Click += new System.EventHandler(this.ConnectButton_Click);
            // 
            // FPSLabel
            // 
            this.FPSLabel.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.FPSLabel.AutoSize = true;
            this.FPSLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 8F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.FPSLabel.Location = new System.Drawing.Point(375, 18);
            this.FPSLabel.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
            this.FPSLabel.Name = "FPSLabel";
            this.FPSLabel.Size = new System.Drawing.Size(27, 13);
            this.FPSLabel.TabIndex = 5;
            this.FPSLabel.Text = "FPS";
            this.FPSLabel.Visible = false;
            // 
            // FPSValue
            // 
            this.FPSValue.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.FPSValue.AutoSize = true;
            this.FPSValue.Location = new System.Drawing.Point(417, 18);
            this.FPSValue.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
            this.FPSValue.Name = "FPSValue";
            this.FPSValue.Size = new System.Drawing.Size(27, 13);
            this.FPSValue.TabIndex = 6;
            this.FPSValue.Text = "FPS";
            this.FPSValue.Visible = false;
            // 
            // FoodLabel
            // 
            this.FoodLabel.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.FoodLabel.AutoSize = true;
            this.FoodLabel.Location = new System.Drawing.Point(375, 42);
            this.FoodLabel.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
            this.FoodLabel.Name = "FoodLabel";
            this.FoodLabel.Size = new System.Drawing.Size(31, 13);
            this.FoodLabel.TabIndex = 7;
            this.FoodLabel.Text = "Food";
            this.FoodLabel.Visible = false;
            // 
            // FoodValue
            // 
            this.FoodValue.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.FoodValue.AutoSize = true;
            this.FoodValue.Location = new System.Drawing.Point(417, 42);
            this.FoodValue.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
            this.FoodValue.Name = "FoodValue";
            this.FoodValue.Size = new System.Drawing.Size(31, 13);
            this.FoodValue.TabIndex = 8;
            this.FoodValue.Text = "none";
            this.FoodValue.Visible = false;
            // 
            // MassLabel
            // 
            this.MassLabel.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.MassLabel.AutoSize = true;
            this.MassLabel.Location = new System.Drawing.Point(375, 66);
            this.MassLabel.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
            this.MassLabel.Name = "MassLabel";
            this.MassLabel.Size = new System.Drawing.Size(32, 13);
            this.MassLabel.TabIndex = 9;
            this.MassLabel.Text = "Mass";
            this.MassLabel.Visible = false;
            // 
            // MassValue
            // 
            this.MassValue.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.MassValue.AutoSize = true;
            this.MassValue.Location = new System.Drawing.Point(417, 66);
            this.MassValue.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
            this.MassValue.Name = "MassValue";
            this.MassValue.Size = new System.Drawing.Size(32, 13);
            this.MassValue.TabIndex = 10;
            this.MassValue.Text = "Mass";
            this.MassValue.Visible = false;
            // 
            // WidthLabel
            // 
            this.WidthLabel.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.WidthLabel.AutoSize = true;
            this.WidthLabel.Location = new System.Drawing.Point(375, 92);
            this.WidthLabel.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
            this.WidthLabel.Name = "WidthLabel";
            this.WidthLabel.Size = new System.Drawing.Size(35, 13);
            this.WidthLabel.TabIndex = 11;
            this.WidthLabel.Text = "Width";
            this.WidthLabel.Visible = false;
            // 
            // WidthValue
            // 
            this.WidthValue.Anchor = ((System.Windows.Forms.AnchorStyles)((System.Windows.Forms.AnchorStyles.Top | System.Windows.Forms.AnchorStyles.Right)));
            this.WidthValue.AutoSize = true;
            this.WidthValue.Location = new System.Drawing.Point(417, 92);
            this.WidthValue.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
            this.WidthValue.Name = "WidthValue";
            this.WidthValue.Size = new System.Drawing.Size(35, 13);
            this.WidthValue.TabIndex = 12;
            this.WidthValue.Text = "Width";
            this.WidthValue.Visible = false;
            // 
            // DiedLabel
            // 
            this.DiedLabel.Anchor = System.Windows.Forms.AnchorStyles.None;
            this.DiedLabel.AutoSize = true;
            this.DiedLabel.Font = new System.Drawing.Font("Segoe UI Black", 40F, System.Drawing.FontStyle.Italic, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.DiedLabel.ForeColor = System.Drawing.Color.Red;
            this.DiedLabel.Location = new System.Drawing.Point(90, 101);
            this.DiedLabel.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
            this.DiedLabel.Name = "DiedLabel";
            this.DiedLabel.Size = new System.Drawing.Size(286, 72);
            this.DiedLabel.TabIndex = 13;
            this.DiedLabel.Text = "You Died!";
            this.DiedLabel.Visible = false;
            // 
            // TitleLabel
            // 
            this.TitleLabel.Anchor = System.Windows.Forms.AnchorStyles.None;
            this.TitleLabel.AutoSize = true;
            this.TitleLabel.Font = new System.Drawing.Font("Gill Sans Ultra Bold", 40F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.TitleLabel.ForeColor = System.Drawing.Color.Red;
            this.TitleLabel.Location = new System.Drawing.Point(22, 28);
            this.TitleLabel.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
            this.TitleLabel.Name = "TitleLabel";
            this.TitleLabel.Size = new System.Drawing.Size(317, 76);
            this.TitleLabel.TabIndex = 14;
            this.TitleLabel.Text = "AgCubio";
            // 
            // MaxMassLabel
            // 
            this.MaxMassLabel.Anchor = System.Windows.Forms.AnchorStyles.None;
            this.MaxMassLabel.AutoSize = true;
            this.MaxMassLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 11F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.MaxMassLabel.Location = new System.Drawing.Point(131, 257);
            this.MaxMassLabel.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
            this.MaxMassLabel.Name = "MaxMassLabel";
            this.MaxMassLabel.Size = new System.Drawing.Size(138, 18);
            this.MaxMassLabel.TabIndex = 15;
            this.MaxMassLabel.Text = "Your Highest Mass:";
            this.MaxMassLabel.Visible = false;
            // 
            // MaxMassValue
            // 
            this.MaxMassValue.Anchor = System.Windows.Forms.AnchorStyles.None;
            this.MaxMassValue.AutoSize = true;
            this.MaxMassValue.Font = new System.Drawing.Font("Microsoft Sans Serif", 11F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.MaxMassValue.Location = new System.Drawing.Point(273, 258);
            this.MaxMassValue.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
            this.MaxMassValue.Name = "MaxMassValue";
            this.MaxMassValue.Size = new System.Drawing.Size(45, 18);
            this.MaxMassValue.TabIndex = 16;
            this.MaxMassValue.Text = "Mass";
            this.MaxMassValue.Visible = false;
            // 
            // MaxWidthLabel
            // 
            this.MaxWidthLabel.Anchor = System.Windows.Forms.AnchorStyles.None;
            this.MaxWidthLabel.AutoSize = true;
            this.MaxWidthLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 11F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.MaxWidthLabel.Location = new System.Drawing.Point(131, 274);
            this.MaxWidthLabel.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
            this.MaxWidthLabel.Name = "MaxWidthLabel";
            this.MaxWidthLabel.Size = new System.Drawing.Size(139, 18);
            this.MaxWidthLabel.TabIndex = 17;
            this.MaxWidthLabel.Text = "Your Highest Width:";
            this.MaxWidthLabel.Visible = false;
            // 
            // MaxWidthValue
            // 
            this.MaxWidthValue.Anchor = System.Windows.Forms.AnchorStyles.None;
            this.MaxWidthValue.AutoSize = true;
            this.MaxWidthValue.Font = new System.Drawing.Font("Microsoft Sans Serif", 11F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.MaxWidthValue.Location = new System.Drawing.Point(273, 274);
            this.MaxWidthValue.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
            this.MaxWidthValue.Name = "MaxWidthValue";
            this.MaxWidthValue.Size = new System.Drawing.Size(46, 18);
            this.MaxWidthValue.TabIndex = 18;
            this.MaxWidthValue.Text = "Width";
            this.MaxWidthValue.Visible = false;
            // 
            // KingLabel
            // 
            this.KingLabel.Anchor = System.Windows.Forms.AnchorStyles.None;
            this.KingLabel.AutoSize = true;
            this.KingLabel.Font = new System.Drawing.Font("Microsoft Sans Serif", 11F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.KingLabel.Location = new System.Drawing.Point(131, 291);
            this.KingLabel.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
            this.KingLabel.Name = "KingLabel";
            this.KingLabel.Size = new System.Drawing.Size(129, 18);
            this.KingLabel.TabIndex = 19;
            this.KingLabel.Text = "King of the Cubes:";
            this.KingLabel.Visible = false;
            // 
            // KingValue
            // 
            this.KingValue.Anchor = System.Windows.Forms.AnchorStyles.None;
            this.KingValue.AutoSize = true;
            this.KingValue.Font = new System.Drawing.Font("Microsoft Sans Serif", 11F, System.Drawing.FontStyle.Regular, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.KingValue.Location = new System.Drawing.Point(273, 291);
            this.KingValue.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
            this.KingValue.Name = "KingValue";
            this.KingValue.Size = new System.Drawing.Size(37, 18);
            this.KingValue.TabIndex = 20;
            this.KingValue.Text = "King";
            this.KingValue.Visible = false;
            // 
            // RetryButton
            // 
            this.RetryButton.Anchor = System.Windows.Forms.AnchorStyles.None;
            this.RetryButton.Location = new System.Drawing.Point(144, 227);
            this.RetryButton.Margin = new System.Windows.Forms.Padding(2);
            this.RetryButton.Name = "RetryButton";
            this.RetryButton.Size = new System.Drawing.Size(167, 23);
            this.RetryButton.TabIndex = 21;
            this.RetryButton.Text = "Try Again!";
            this.RetryButton.UseVisualStyleBackColor = true;
            this.RetryButton.Visible = false;
            this.RetryButton.Click += new System.EventHandler(this.RetryButton_Click);
            // 
            // ConnectionLabel
            // 
            this.ConnectionLabel.Anchor = System.Windows.Forms.AnchorStyles.None;
            this.ConnectionLabel.AutoSize = true;
            this.ConnectionLabel.Font = new System.Drawing.Font("Segoe UI", 32F, System.Drawing.FontStyle.Italic, System.Drawing.GraphicsUnit.Point, ((byte)(0)));
            this.ConnectionLabel.ForeColor = System.Drawing.Color.Red;
            this.ConnectionLabel.Location = new System.Drawing.Point(18, 170);
            this.ConnectionLabel.Margin = new System.Windows.Forms.Padding(2, 0, 2, 0);
            this.ConnectionLabel.Name = "ConnectionLabel";
            this.ConnectionLabel.Size = new System.Drawing.Size(427, 59);
            this.ConnectionLabel.TabIndex = 22;
            this.ConnectionLabel.Text = "No Server Connection";
            this.ConnectionLabel.Visible = false;
            // 
            // Form1
            // 
            this.AutoScaleDimensions = new System.Drawing.SizeF(6F, 13F);
            this.AutoScaleMode = System.Windows.Forms.AutoScaleMode.Font;
            this.ClientSize = new System.Drawing.Size(465, 333);
            this.Controls.Add(this.RetryButton);
            this.Controls.Add(this.TitleLabel);
            this.Controls.Add(this.WidthValue);
            this.Controls.Add(this.WidthLabel);
            this.Controls.Add(this.MassValue);
            this.Controls.Add(this.MassLabel);
            this.Controls.Add(this.FoodValue);
            this.Controls.Add(this.FoodLabel);
            this.Controls.Add(this.FPSValue);
            this.Controls.Add(this.FPSLabel);
            this.Controls.Add(this.ConnectButton);
            this.Controls.Add(this.ServerLabel);
            this.Controls.Add(this.PlayerLabel);
            this.Controls.Add(this.ServerBox);
            this.Controls.Add(this.PlayerBox);
            this.Controls.Add(this.DiedLabel);
            this.Controls.Add(this.KingValue);
            this.Controls.Add(this.KingLabel);
            this.Controls.Add(this.MaxWidthValue);
            this.Controls.Add(this.MaxWidthLabel);
            this.Controls.Add(this.MaxMassValue);
            this.Controls.Add(this.MaxMassLabel);
            this.Controls.Add(this.ConnectionLabel);
            this.Margin = new System.Windows.Forms.Padding(2);
            this.Name = "Form1";
            this.Text = "Form1";
            this.Paint += new System.Windows.Forms.PaintEventHandler(this.Form1_Paint);
            this.KeyDown += new System.Windows.Forms.KeyEventHandler(this.Form1_KeyDown);
            this.ResumeLayout(false);
            this.PerformLayout();

        }

        #endregion

        private System.Windows.Forms.TextBox PlayerBox;
        private System.Windows.Forms.TextBox ServerBox;
        private System.Windows.Forms.Label PlayerLabel;
        private System.Windows.Forms.Label ServerLabel;
        private System.Windows.Forms.Button ConnectButton;
        private System.Windows.Forms.Label FPSLabel;
        private System.Windows.Forms.Label FPSValue;
        private System.Windows.Forms.Label FoodLabel;
        private System.Windows.Forms.Label FoodValue;
        private System.Windows.Forms.Label MassLabel;
        private System.Windows.Forms.Label MassValue;
        private System.Windows.Forms.Label WidthLabel;
        private System.Windows.Forms.Label WidthValue;
        private System.Windows.Forms.Label DiedLabel;
        private System.Windows.Forms.Label TitleLabel;
        private System.Windows.Forms.Label MaxMassLabel;
        private System.Windows.Forms.Label MaxMassValue;
        private System.Windows.Forms.Label MaxWidthLabel;
        private System.Windows.Forms.Label MaxWidthValue;
        private System.Windows.Forms.Label KingLabel;
        private System.Windows.Forms.Label KingValue;
        private System.Windows.Forms.Button RetryButton;
        private System.Windows.Forms.Label ConnectionLabel;
    }
}

