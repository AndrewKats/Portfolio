using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
using AgCubio;
using System.Net.Sockets;
using Newtonsoft.Json;
using System.Diagnostics;
using View;

namespace AgCubio {
    public partial class Form1 : Form {
        /// <summary>
        /// A brush for drawing cubes and names
        /// </summary>
        private System.Drawing.SolidBrush myBrush;

        /// <summary>
        /// The world of the game
        /// </summary>
        private World world;

        /// <summary>
        /// The preserved state object
        /// </summary>
        private PreservedState gState;

        /// <summary>
        /// Determines if we are ready to paint
        /// </summary>
        private bool paintIt;

        /// <summary>
        /// Determines if we should paint the death screen
        /// </summary>
        private bool paintDied;

        /// <summary>
        /// Determines if we have gotten the name from the PlayerBox
        /// </summary>
        private bool nameIt;

        /// <summary>
        /// The player's uid
        /// </summary>
        private int playerId;

        /// <summary>
        /// The scale factor for painting
        /// </summary>
        private readonly double scaler;

        /// <summary>
        /// Stores the FPS for display
        /// </summary>
        private int[] fps;

        /// <summary>
        /// Timer for measuring FPS
        /// </summary>
        private Stopwatch timer;

        /// <summary>
        /// A set of your team members
        /// </summary>
        private HashSet<int> team;

        /// <summary>
        /// Your highest mass of this life
        /// </summary>
        private double maxMass = 0;

        /// <summary>
        /// Determines if a server error has occured
        /// </summary>
        private bool serverError = false;

        /// <summary>
        /// Determines if connect has been clicked
        /// </summary>
        private bool connectClicked = false;

        /// <summary>
        /// Determines if the death screen has been displayed
        /// </summary>
        private bool deathDisplayed = false;

        /// <summary>
        /// Construct the GUI and initialize variables
        /// </summary>
        public Form1() {
            world = new World();
            paintIt = false;
            paintDied = false;
            nameIt = false;

            playerId = -1;

            // The scale factor
            scaler = 10;

            fps = new int[] { 0, 0 };

            team = new HashSet<int>();

            InitializeComponent();

            // Double buffer to avoid flickering
            DoubleBuffered = true;

            // Start the timer
            timer = new Stopwatch();
            timer.Start();
        }

        /// <summary>
        /// Updates the world with all the new cubes from the json
        /// </summary>
        /// <param name="state"> The state object of the network controller</param>
        public void UpdateWorld(PreservedState state) {
            // If there's a serer error, set serverError to true
            if (state.error) {
                serverError = true;
            }
            // If no error, update the world with the json in the state's stringbuilder
            else {
                string stringbuilder = (state.sb).ToString();

                List<string> jsons = new List<string>(stringbuilder.Split('\n'));

                string lastjson = jsons[jsons.Count - 1];

                state.sb.Clear();
                if (!lastjson.EndsWith("}")) {
                    lock (state.sb) {
                        state.sb.Append(lastjson);
                    }
                    jsons.RemoveAt(jsons.Count - 1);
                }

                // Put a lock on the updating in world
                lock (world) {
                    world.Update(jsons, ref playerId, team);
                }

                // Set the callback and ask for more data
                state.Callback = UpdateWorld;
                Client.i_want_more_data(state);
            }
        }

        /// <summary>
        /// Paints the cubes and other GUI elements
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void Form1_Paint(object sender, PaintEventArgs e) {
            // Lock the world while the game is painted
            lock (world) {
                // If there's a server error, display the appropriate GUI elements
                if (serverError && !paintDied) {
                    FPSLabel.Visible = false;
                    FPSValue.Visible = false;
                    FoodLabel.Visible = false;
                    FoodValue.Visible = false;
                    MassLabel.Visible = false;
                    MassValue.Visible = false;
                    WidthLabel.Visible = false;
                    WidthValue.Visible = false;
                    ConnectionLabel.Visible = true;
                    RetryButton.Visible = true;
                }
                // If no error, paint the cubes
                else {
                    bool ImAlive = false;
                    if (world.cubes.ContainsKey(playerId)) {
                        ImAlive = true;
                        paintDied = false;
                    }

                    if (ImAlive) {
                        // Update the player's highest mass and width
                        if (world.cubes[playerId].Mass > maxMass) {
                            maxMass = world.cubes[playerId].Mass;
                            MaxMassValue.Text = "" + (int)maxMass;
                            MaxWidthValue.Text = "" + (int)world.cubes[playerId].width;
                        }

                        paintDied = false;
                        double scaleWidth, distanceFromCenterX, distanceFromCenterY;

                        // Set team dimensions
                        SetDimensions(out scaleWidth, out distanceFromCenterX, out distanceFromCenterY);
                        // Draw the cubes
                        DrawCubes(scaleWidth, distanceFromCenterX, distanceFromCenterY, e);
                        // Update the stat labels
                        ResetLabels();

                        // Send a move request based on relative scaled mouse position
                        if (gState.stateSocket != null && gState.stateSocket.Connected == true && nameIt == true) {
                            Point mousePoint = PointToClient(new Point(MousePosition.X, MousePosition.Y));
                            double xpos = (int)((((mousePoint.X - distanceFromCenterX)) / scaleWidth));
                            double ypos = (int)((((mousePoint.Y - distanceFromCenterY)) / scaleWidth));
                            Client.Send(gState, "(move, " + xpos + ", " + ypos + ")\n");
                        }
                        if (gState.stateSocket != null && gState.stateSocket.Connected == true) {
                            paintDied = true;
                        }
                    }
                    // In case of death
                    else {
                        if (paintIt && paintDied) {
                            Cube King = new Cube();
                            King.Name = "None";
                            King.Mass = 0;
                            double largest = 0;
                            foreach (Cube c in world.cubes.Values) {
                                if (!c.food) {
                                    if (c.Mass > largest) {
                                        King = c;
                                        largest = c.Mass;
                                    }
                                }
                            }

                            KingValue.Text = "" + King.Name + " (Mass " + (int)King.Mass + ")";
                            KingLabel.Visible = true;
                            KingValue.Visible = true;
                            KingLabel.Refresh();
                            KingValue.Refresh();

                            // Update labels for the post-game screen
                            DeathLabels();

                            try {
                                gState.stateSocket.Shutdown(SocketShutdown.Both);
                                gState.stateSocket.Close();
                            }
                            catch (Exception ex) {
                                Console.WriteLine(ex.Message);
                            }
                        }
                    }
                    // If alive and connected, invalidate the GUI
                    if (paintIt && !deathDisplayed) {
                        Invalidate();
                    }
                }
            }
            // Set the current FPS
            SetFPS();
        }

        /// <summary>
        /// Sets the dimensions of our team
        /// </summary>
        /// <param name="scaleWidth"> The amount to scale by</param>
        /// <param name="distanceFromCenterX"> Horizontal distance of cube from the center</param>
        /// <param name="distanceFromCenterY"> Vertical distance of cube from the center</param>
        private void SetDimensions(out double scaleWidth, out double distanceFromCenterX, out double distanceFromCenterY) {
            Cube me = world.cubes[playerId];
            double leftWidth = me.left;
            double rightWidth = me.right;
            double teamCenterX = 0;
            double teamCenterY = 0;
            double teamCount = team.Count;

            // If only one player on team (not split)
            if (teamCount == 0 || teamCount == 1) {
                teamCenterX = me.centerX;
                teamCenterY = me.centerY;
            }
            // If more than one player on team (split)
            else {
                LinkedList<int> remove = new LinkedList<int>();
                foreach (int cube in team) {
                    // Find the leftmost and rightmost cubes on team
                    // Also calculate the center of all the team cubes
                    if (world.cubes.ContainsKey(cube)) {
                        Cube c = world.cubes[cube];
                        if (c.left < leftWidth) {
                            leftWidth = c.left;
                        }
                        if (c.right > rightWidth) {
                            rightWidth = c.right;
                        }
                        teamCenterX += c.centerX;
                        teamCenterY += c.centerY;
                    }
                    else
                        remove.AddLast(cube);
                }

                teamCount -= remove.Count;
                teamCenterX /= teamCount;
                teamCenterY /= teamCount;

                foreach (int c in remove) {
                    team.Remove(c);
                }
            }

            // Calculate the scale width
            scaleWidth = (this.Width / ((rightWidth - leftWidth) * scaler));

            // Calculate distances from center
            distanceFromCenterX = (this.Width / 2.0 - (teamCenterX) * scaleWidth + (rightWidth - leftWidth) / 2 * scaleWidth);
            distanceFromCenterY = (this.Height / 2.0 - (teamCenterY) * scaleWidth + (rightWidth - leftWidth) / 2 * scaleWidth);
        }

        /// <summary>
        /// Determines what happens when connect is clicked
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void ConnectButton_Click(object sender, EventArgs e) {
            ClickConnect();
        }

        /// <summary>
        /// The actions that should be performed when connecting
        /// </summary>
        private void ClickConnect() {
            // Pass the name to the network controller
            gState = Client.Connect_to_Server(ConnectCallBack, ServerBox.Text);

            // Make GUI elements invisible
            ServerBox.Visible = false;
            ServerBox.Enabled = false;
            PlayerBox.Visible = false;
            PlayerBox.Enabled = false;
            ServerLabel.Visible = false;
            PlayerLabel.Visible = false;
            ConnectButton.Visible = false;
            ConnectButton.Enabled = false;
            TitleLabel.Visible = false;
            TitleLabel.Enabled = false;

            // Make game stats visible
            FPSLabel.Visible = true;
            FPSValue.Visible = true;
            FoodLabel.Visible = true;
            FoodValue.Visible = true;
            MassLabel.Visible = true;
            MassValue.Visible = true;
            WidthLabel.Visible = true;
            WidthValue.Visible = true;

            // We should start painting the cubes
            paintIt = true;

            // Connect has been clicked
            connectClicked = true;
        }

        /// <summary>
        /// A callback for connecting and sending the player's name
        /// </summary>
        /// <param name="state"> The state object of the network controller</param>
        private void ConnectCallBack(PreservedState state) {
            // If the network is in an error state, set serverError to true
            if (state.error) {
                serverError = true;
            }

            // Get the first cubes data and request more data
            state.Callback = GetFirstData;
            Client.i_want_more_data(state);
            // Send player name
            Client.Send(state, PlayerBox.Text + "\n");
            // Name has been set
            nameIt = true;
        }

        /// <summary>
        /// Calls for updating the world with the player cube
        /// </summary>
        /// <param name="state"> The state object of the network controller</param>
        private void GetFirstData(PreservedState state) {
            UpdateWorld(state);
        }

        /// <summary>
        /// If SPACE is pressed, split
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void Form1_KeyDown(object sender, KeyEventArgs e) {
            if (e.KeyCode == Keys.Space) {
                if (gState.stateSocket != null && gState.stateSocket.Connected == true) {
                    // Lock the world while a split request is created and sent
                    lock (world) {
                        Cube me = world.cubes[playerId];
                        double leftWidth = me.left;
                        double rightWidth = me.right;
                        double teamCenterX = 0;
                        double teamCenterY = 0;
                        double teamCount = 0;

                        foreach (Cube c in world.cubes.Values) {
                            if (c.team_id == me.team_id && c.team_id == me.uid && !c.food) {
                                teamCount++;
                                if (c.left < leftWidth) {
                                    leftWidth = c.left;
                                }
                                if (c.right > rightWidth) {
                                    rightWidth = c.right;
                                }
                                teamCenterX += c.centerX;
                                teamCenterY += c.centerY;

                            }
                        }
                        if (teamCount == 0) {
                            teamCount = 1;
                            teamCenterX = me.centerX;
                            teamCenterY = me.centerY;
                        }
                        else {
                            teamCenterX = teamCenterX / teamCount;
                            teamCenterY = teamCenterY / teamCount;
                        }

                        // Make calculations for scaling and centering
                        double scaleWidth = (this.Width / ((rightWidth - leftWidth) * scaler));
                        double distanceFromCenterX = (this.Width / 2.0 - teamCenterX * scaleWidth);
                        double distanceFromCenterY = (this.Height / 2.0 - teamCenterY * scaleWidth);

                        // Calculate relative scaled mouse position and send the split request
                        Point mousePoint = PointToClient(new Point(MousePosition.X, MousePosition.Y));
                        double xpos = (int)((((mousePoint.X - distanceFromCenterX)) / scaleWidth));
                        double ypos = (int)((((mousePoint.Y - distanceFromCenterY)) / scaleWidth));
                        Client.Send(gState, "(split, " + xpos + ", " + ypos + ")\n");
                    }
                }
                // Nobody likes that beep
                e.SuppressKeyPress = true;
            }
        }

        /// <summary>
        /// Set the game's current FPS
        /// </summary>
        private void SetFPS() {
            fps[1]++;
            if (timer.ElapsedMilliseconds >= 500) {
                fps[0] = fps[1];
                fps[1] = 0;
                timer.Reset();
                timer.Start();
            }
        }

        /// <summary>
        /// Draw all the cubes in the world (and their names)
        /// </summary>
        /// <param name="scaleWidth"> The amount to scale the cubes</param>
        /// <param name="distanceFromCenterX"> Amount to center horizontally</param>
        /// <param name="distanceFromCenterY"> Amount to center vertically</param>
        /// <param name="e"></param>
        private void DrawCubes(double scaleWidth, double distanceFromCenterX, double distanceFromCenterY, PaintEventArgs e) {
            // Go through and paint every cube
            foreach (Cube c in world.cubes.Values) {
                // If it's food, don't bother with drawing the blank name
                if (c.food) {
                    Color color = Color.FromArgb(c.argb_color);
                    myBrush = new System.Drawing.SolidBrush(color);
                    // Draw the food
                    e.Graphics.FillRectangle(myBrush, new Rectangle((int)((((c.loc_x - (c.width / 2)) * scaleWidth)) + distanceFromCenterX), (int)((((c.loc_y - (c.width / 2)) * scaleWidth)) + distanceFromCenterY), (int)((c.width + 3) * scaleWidth), (int)((c.width + 3) * scaleWidth)));
                }
                // If player, draw the name
                else {
                    Color color = Color.FromArgb(c.argb_color);
                    myBrush = new System.Drawing.SolidBrush(color);
                    // Draw the cube
                    e.Graphics.FillRectangle(myBrush, new Rectangle((int)((((c.loc_x - (c.width / 2)) * scaleWidth)) + distanceFromCenterX), (int)((((c.loc_y - (c.width / 2)) * scaleWidth)) + distanceFromCenterY), (int)(c.width * scaleWidth), (int)(c.width * scaleWidth)));
                    Color yellowColor = Color.FromArgb(255, 255, 0);
                    // Make the name yellow
                    myBrush = new System.Drawing.SolidBrush(yellowColor);

                    // Add spaces to center a name
                    string nameToDraw = c.Name;
                    if (c.Name.Length < 11) {
                        int spaces = (int)((10 - c.Name.Length));
                        while (spaces > 0) {
                            nameToDraw = " " + nameToDraw;
                            spaces--;
                        }
                    }
                    // Draw the name
                    e.Graphics.DrawString(nameToDraw, new Font("Arial", (float)((c.width / 7.0) * scaleWidth), FontStyle.Regular, GraphicsUnit.Pixel), myBrush, (float)((((c.loc_x - (c.width / 2.2)) * scaleWidth)) + distanceFromCenterX), (float)((((c.loc_y + (c.width / 20.0) - (c.width / 10.0)) * scaleWidth)) + distanceFromCenterY));
                }
            }
        }

        /// <summary>
        /// Resets and refreshes the labels during gameplay
        /// </summary>
        private void ResetLabels() {
            // Set label values
            if (world.cubes.ContainsKey(playerId)) {
                FPSValue.Text = "" + fps[0];
                FoodValue.Text = "" + world.foodCount;
                MassValue.Text = "" + (int)world.cubes[playerId].Mass;
                WidthValue.Text = "" + (int)world.cubes[playerId].width;
            }

            // Refresh all the labels
            FPSLabel.Refresh();
            FPSValue.Refresh();

            FoodLabel.Refresh();
            FoodValue.Refresh();

            MassLabel.Refresh();
            MassValue.Refresh();

            WidthLabel.Refresh();
            WidthValue.Refresh();
        }

        /// <summary>
        /// Makes visible and refreshes labels for the post-game screen
        /// </summary>
        private void DeathLabels() {
            DiedLabel.Visible = true;
            DiedLabel.Refresh();

            MaxMassLabel.Visible = true;
            MaxMassValue.Visible = true;
            MaxMassLabel.Refresh();
            MaxMassValue.Refresh();

            MaxWidthLabel.Visible = true;
            MaxWidthValue.Visible = true;
            MaxWidthLabel.Refresh();
            MaxWidthValue.Refresh();

            FPSLabel.Visible = false;
            FPSValue.Visible = false;

            FoodLabel.Visible = false;
            FoodValue.Visible = false;

            MassLabel.Visible = false;
            MassValue.Visible = false;

            WidthLabel.Visible = false;
            WidthValue.Visible = false;

            RetryButton.Visible = true;
            deathDisplayed = true;
        }

        /// <summary>
        /// Determines what happens when the retry button is clicked.
        /// A new window is opened and the old one is closed.
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void RetryButton_Click(object sender, EventArgs e) {
            Form1 newForm = new Form1();

            MyApplicationContext.getAppContext().RunForm(newForm);

            this.Close();
        }

        /// <summary>
        /// When ENTER is pressed, enter game
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void ServerBox_KeyDown(object sender, KeyEventArgs e) {
            if (e.KeyCode == Keys.Return) {
                // Connect the game if it isn't connected
                if (!connectClicked) {
                    ClickConnect();
                }
                // Get those beeps outta here
                e.SuppressKeyPress = true;
            }
        }

        /// <summary>
        /// When ENTER is pressed, enter game
        /// </summary>
        /// <param name="sender"></param>
        /// <param name="e"></param>
        private void PlayerBox_KeyDown(object sender, KeyEventArgs e) {
            if (e.KeyCode == Keys.Return) {
                // Connect the game if it isn't connected
                if (!connectClicked) {
                    ClickConnect();
                }
                // Sweet silence
                e.SuppressKeyPress = true;
            }
        }
    }
}
