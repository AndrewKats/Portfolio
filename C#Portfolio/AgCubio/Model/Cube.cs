
using Newtonsoft.Json;
using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace AgCubio
{
    /// <summary>
    /// Class which represents a cube object 
    /// </summary>
    [JsonObject(MemberSerialization.OptIn)]
    public class Cube
    {
        /// <summary>
        /// x coordinate of the cube 
        /// </summary>
        [JsonProperty]
        public double loc_x
        {
            get; set;
        }
        /// <summary>
        /// y coordinate of the cube
        /// </summary>
        [JsonProperty]
        public double loc_y
        {
            get; set;
        }
        /// <summary>
        /// mass value for the cube 
        /// </summary>
        [JsonProperty]
        public double Mass
        {
            get; set;
        }
        /// <summary>
        /// name of the cube
        /// </summary>
        [JsonProperty]
        public String Name
        {
            get; set;
        }
        /// <summary>
        /// color of the cube
        /// </summary>
        [JsonProperty]
        public int argb_color
        {
            get; set;
        }
        /// <summary>
        /// whether the cube is food
        /// </summary>
        [JsonProperty]
        public bool food
        {
            get; private set;
        }
        /// <summary>
        /// unique id for the cube
        /// </summary>
        [JsonProperty]
        public int uid;

        /// <summary>
        /// team_id for the cube
        /// </summary>
        [JsonProperty]
        public int team_id;

        /// <summary>
        /// Radius of the collision circle of a cube
        /// </summary>
        public double collisionRadius { get { return width / 4; } private set { width = value * 4; } }

        /// <summary>
        /// width of the cube
        /// </summary>
        public double width { get { return Math.Sqrt(Mass); } private set { Mass = Math.Pow(value, 2); } }

        /// <summary>
        /// The left side of this cube
        /// </summary>
        public double top { get { return loc_y - width / 2.0; } private set { loc_y = value + width / 2.0; } }

        /// <summary>
        /// The left side of this cube
        /// </summary>
        public double bottom { get { return loc_y + width / 2.0; } private set { loc_y = value - width / 2.0; } }

        /// <summary>
        /// The left side of this cube
        /// </summary>
        public double left { get { return loc_x - width / 2.0; } private set { loc_x = value + width / 2.0; } }

        /// <summary>
        /// The right side of this cube
        /// </summary>
        public double right { get { return loc_x + width / 2.0; } private set { loc_x = value - width / 2.0; } }

        /// <summary>
        /// The frames this cube has to boost
        /// </summary>
        public int boostFrames;

        /// <summary>
        /// Determines if a cube is being boosted from a split
        /// </summary>
        public bool isBoosted;

        /// <summary>
        /// The time since a split
        /// </summary>
        public Stopwatch splitTime;

        /// <summary>
        /// Constructor for the cube
        /// </summary>
        /// <param name="loc_x"> The horizontal location</param>
        /// <param name="loc_y"> The vertical location</param>
        /// <param name="argb_color"> The argb color</param>
        /// <param name="uid"> The unique ID</param>
        /// <param name="team_id"> The team ID</param>
        /// <param name="food"> Whether this cube is food</param>
        /// <param name="Name"> The name of this cube</param>
        /// <param name="Mass"> The mass of this cube</param>
        [JsonConstructor]
        public Cube(double loc_x, double loc_y, int argb_color, int uid, int team_id, bool food, String Name, double Mass)
        {
            this.loc_x = loc_x;
            this.loc_y = loc_y;
            this.Mass = Mass;
            this.argb_color = argb_color;
            this.food = food;
            this.uid = uid;
            this.Name = Name;
            this.team_id = team_id;
            this.boostFrames = 0;
            splitTime = new Stopwatch();
        }
    }
}