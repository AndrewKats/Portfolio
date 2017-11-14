using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace AgCubio
{
    /// <summary>
    /// The world containing the cubes
    /// </summary>
    public class World
    {
        /// <summary>
        /// The horizontal width of the world
        /// </summary>
        public readonly double Width;

        /// <summary>
        /// The vertical height of the world
        /// </summary>
        public readonly double Height;

        /// <summary>
        /// The amount of food currently in the world
        /// </summary>
        public int foodCount { get; private set; }

        /// <summary>
        /// All the cubes contained in this world
        /// </summary>
        public Dictionary<int, Cube> cubes { get; private set; }

        /// <summary>
        /// Constructor for World that initializes foodCount, Width, Height, and cubes
        /// </summary>
        public World()
        {
            foodCount = 0;
            Width = 1000;
            Height = 1000;
            cubes = new Dictionary<int, Cube>();
        }

        /// <summary>
        /// Updates the cells currently in the world based on json messages
        /// </summary>
        /// <param name="jsons"> A list of json strings to parse</param>
        /// <param name="myID"> The player uid</param>
        /// <param name="team"> A set of all cubes belonging to the player when split </param>
        public void Update(List<string> jsons, ref int myID, HashSet<int> team)
        {

            foreach (string json in jsons)
            {
                if (json != "")
                {
                    // Create a new cue from the json string
                    Cube cube = JsonConvert.DeserializeObject<Cube>(json + "\n");

                    // Check the ID of the cube
                    int id = cube.uid;
                    if (myID == -1)
                        myID = id;

                    // If a cube is mass 0, remove it from the world
                    if (cube.Mass == 0)
                    {
                        bool isFood = false;
                        if (cube.food)
                        {
                            isFood = true;
                        }
                        if (cubes.Remove(id))
                        {
                            // If a removed cell is food, decrement foodCount
                            if (isFood)
                            {
                                foodCount--;
                            }
                        }

                    }
                    // If the current cube exists, update it
                    else if (cubes.ContainsKey(id) && !cube.food)
                    {
                        cubes[id] = cube;
                        if (cube.team_id == myID)
                            team.Add(id);
                    }
                    // If the current cube doesn't exist, add it
                    else if (!cubes.ContainsKey(id))
                    {
                        cubes.Add(id, cube);

                        if (cube.food)
                            foodCount++;

                        else if (cube.team_id == myID)
                            team.Add(id);
                    }
                }
            }
        }
    }


    /// <summary>
    /// Represents a cube with a mass, width, position, and identification
    /// </summary>
    [JsonObject(MemberSerialization.OptIn)]
    public class Cube
    {
        /// <summary>
        /// The unique ID of this cube
        /// </summary>
        [JsonProperty]
        public int uid;

        /// <summary>
        /// The ID of this cube's team
        /// </summary>
        [JsonProperty]
        public int team_id;

        /// <summary>
        /// The x (left) position of this cube
        /// </summary>
        [JsonProperty]
        public double loc_x;

        /// <summary>
        /// The y (top) position of this cube
        /// </summary>
        [JsonProperty]
        public double loc_y;

        /// <summary>
        /// The name of this cube
        /// </summary>
        [JsonProperty]
        public string Name;

        /// <summary>
        /// The mass of this cube
        /// </summary>
        [JsonProperty]
        public double Mass { get; set; }

        /// <summary>
        /// The color of this cube
        /// </summary>
        [JsonProperty]
        public int argb_color;

        /// <summary>
        /// Whether this cube is food (or a player)
        /// </summary>
        [JsonProperty]
        public bool food;

        /// <summary>
        /// The width of this cube based on mass.
        /// Width = Mass^0.65
        /// </summary>
        public double width { get { return Math.Sqrt(Mass); } private set { Mass = Math.Pow(value, 2); } }

        /// <summary>
        /// The position of the top of this cube (same as y location)
        /// </summary>
        public double top { get { return loc_y; } private set { loc_y = value; } }

        /// <summary>
        /// The position of the left of this cube (same as x location)
        /// </summary>
        public double left { get { return loc_x; } private set { loc_x = value; } }

        /// <summary>
        /// The position of the right side of this cube
        /// </summary>
        public double right { get { return loc_x + width; } private set { loc_x = value - width; } }

        /// <summary>
        /// The position of the bottom side of this cube
        /// </summary>
        public double bottom { get { return loc_y + width; } private set { loc_y = value - width; } }

        /// <summary>
        /// The horizontal position of the center of this cube
        /// </summary>
        public double centerX { get { return loc_x + width / 2.0; } private set { loc_x = value - width / 2.0; } }

        /// <summary>
        /// The vertical position of the center of this cube
        /// </summary>
        public double centerY { get { return loc_y + width / 2.0; } private set { loc_y = value - width / 2.0; } }


        /// <summary>
        /// Sets the center of a cube
        /// </summary>
        /// <param name="x"> x position of center</param>
        /// <param name="y"> y position of center</param>
        public void Center(double x, double y)
        {
            centerX = x;
            centerY = y;
        }

        /// <summary>
        /// Sets the top left corner of a cube
        /// </summary>
        /// <param name="x"> x position of corner</param>
        /// <param name="y"> y position of corner</param>
        public void TopLeft(double x, double y)
        {
            left = x;
            right = x + width;
            top = y;
            bottom = y + width;
        }

        /// <summary>
        /// Sets the mass of a cube
        /// </summary>
        /// <param name="mass"> The mass of this cube</param>
        public void ChangeMass(double mass)
        {
            Mass = mass;
            width = Math.Pow(Mass, 0.65);
        }
    }
}
