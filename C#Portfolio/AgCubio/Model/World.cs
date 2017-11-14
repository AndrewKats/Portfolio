using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;
using System.Net.Sockets;
using System.Xml;

namespace AgCubio
{
    /// <summary>
    /// Class that represents the world of the game
    /// </summary>
    public class World
    {
        /// <summary>
        /// World width and height variables
        /// </summary>
        public readonly int worldWidth, worldHeight;

        /// <summary>
        /// Creates dictionary for storing cubes
        /// </summary>
        public Dictionary<int, Cube> foodCubes;

        /// <summary>
        /// Holds the world's viruses
        /// </summary>
        public Dictionary<int, Cube> virusCubes;

        /// <summary>
        /// Holds the players and teams if they split
        /// </summary>
        public Dictionary<int, Dictionary<int, Cube>> teams;

        /// <summary>
        /// The cubes that have changed that we need to send to clients
        /// </summary>
        public HashSet<Cube> cubesToUpdate;

        /// <summary>
        /// Number of food cubes currently in the game
        /// </summary>
        public int foodCount;

        /// <summary>
        /// The amount of viruses in the world
        /// </summary>
        public int virusCount;

        /// <summary>
        /// A variable for storing various colors for cubes
        /// </summary>
        private int color;

        /// <summary>
        /// The refresh rate of the game
        /// </summary>
        public readonly int heartBeatsPerSec;

        /// <summary>
        /// The mass of a food
        /// </summary>
        public readonly double foodMass;

        /// <summary>
        /// The mass of a virus
        /// </summary>
        public readonly double virusMass;

        /// <summary>
        /// The starting mass of a new player
        /// </summary>
        public readonly double playerStart;

        /// <summary>
        /// The most food cubes that can exist at a time
        /// </summary>
        public readonly int maxFood;

        /// <summary>
        /// The minimum mass of a cube allowed to split
        /// </summary>
        public readonly double minSplitMass;

        /// <summary>
        /// The maximum amount of cubes on a team
        /// </summary>
        public readonly int maxSplitCubes;

        /// <summary>
        /// Defines the required relative size of a cube that allows it to eat another
        /// </summary>
        public readonly double absorbConstant;

        /// <summary>
        /// Increments an ID count that increments every time a new uid is assigned
        /// </summary>
        public int uidCount;

        /// <summary>
        /// The time it takes to merge after split
        /// </summary>
        public readonly long minSplitTime;

        /// <summary>
        /// Used to modify speed calculations.
        /// </summary>
        public readonly int speedModifier;

        /// <summary>
        /// Constructor for the world
        /// </summary>
        public World()
        {
            uidCount = 1;
            foodCount = 0;
            foodCubes = new Dictionary<int, Cube>();
            teams = new Dictionary<int, Dictionary<int, Cube>>();
            cubesToUpdate = new HashSet<Cube>();
            virusCubes = new Dictionary<int, Cube>();


            XmlReader reader;

            try { reader = XmlReader.Create(@"..\..\..\..\AgCubio\Resources\world_parameters.xml"); }

            catch { throw new ArgumentException("Invalid file name"); }

            try
            {
                using (reader)
                {
                    while (reader.Read())
                    {
                        if (reader.IsStartElement())
                        {

                            switch (reader.Name)
                            {

                                case "heartBeatsPerSec":
                                    reader.Read();
                                    Int32.TryParse(reader.Value, out heartBeatsPerSec);
                                    break;

                                case "worldWidth":
                                    reader.Read();
                                    Int32.TryParse(reader.Value, out worldWidth);
                                    break;

                                case "worldHeight":
                                    reader.Read();
                                    Int32.TryParse(reader.Value, out worldHeight);
                                    break;

                                case "maxFood":
                                    reader.Read();
                                    Int32.TryParse(reader.Value, out maxFood);
                                    break;

                                case "foodMass":
                                    reader.Read();
                                    Double.TryParse(reader.Value, out foodMass);
                                    break;

                                case "playerStart":
                                    reader.Read();
                                    Double.TryParse(reader.Value, out playerStart);
                                    break;

                                case "minSplitMass":
                                    reader.Read();
                                    Double.TryParse(reader.Value, out minSplitMass);
                                    break;

                                case "virusMass":
                                    reader.Read();
                                    Double.TryParse(reader.Value, out virusMass);
                                    break;

                                case "virusCount":
                                    reader.Read();
                                    Int32.TryParse(reader.Value, out virusCount);
                                    break;

                                case "absorbConstant":
                                    reader.Read();
                                    Double.TryParse(reader.Value, out absorbConstant);
                                    break;

                                case "maxSplitCubes":
                                    reader.Read();
                                    Int32.TryParse(reader.Value, out maxSplitCubes);
                                    break;

                                case "minSplitTime":
                                    reader.Read();
                                    long.TryParse(reader.Value, out minSplitTime);
                                    break;

                                case "speedModifier":
                                    reader.Read();
                                    Int32.TryParse(reader.Value, out speedModifier);
                                    break;
                            }
                        }
                    }
                }
            }
            catch (Exception)
            {
            }
        }

        /// <summary>
        /// Adds the specified amount of food cubes to the world in random locations
        /// </summary>
        /// <param name="amount"> Amount of food cubes to add</param>
        public void addFood(int amount)
        {
            foodCount += amount;
            Random rand = new Random();
            for (int i = 0; i < amount; i++)
            {
                int locx = rand.Next(1, worldWidth - 1);
                int locy = rand.Next(1, worldHeight - 1);
                color = rand.Next(-16777216, -1);
                foodCubes.Add(uidCount, new Cube(locx, locy, color, uidCount, 0, true, "", foodMass));
                uidCount++;
            }

        }

        /// <summary>
        /// Adds the specified amount of virus cubes to the world in random locations
        /// </summary>
        /// <param name="amount"> Amount of virus cubes to add</param>
        public void addVirus(int amount)
        {
            Random rand = new Random();
            for (int i = 0; i < amount; i++)
            {
                int locx = rand.Next((int)Math.Sqrt(virusMass), worldWidth - (int)Math.Sqrt(virusMass));
                int locy = rand.Next((int)Math.Sqrt(virusMass), worldHeight - (int)Math.Sqrt(virusMass));
                color = rand.Next(-16711936, -16711900);
                virusCubes.Add(uidCount, new Cube(locx, locy, color, uidCount, 0, false, "☢ VIRUS ☢", virusMass));
                uidCount++;
            }
        }

        /// <summary>
        /// Slowly shrinks the player over time based on their mass
        /// </summary>
        /// <param name="cube"> The cube to take mass from</param>
        public void AttritionPlayer(Cube cube)
        {
            if (cube.Mass > 200)
            {
                cube.Mass -= cube.Mass / (500 * Math.Pow(cube.Mass, 0.7));
            }
            cubesToUpdate.Add(cube);
        }

        /// <summary>
        /// Update method for deserializing cubes and storing them in the dictionary
        /// </summary>
        /// <param name="json"> Cube info to deserialize</param>
        public void update(String[] json)
        {
            Cube cube;
            foreach (String s in json)
            {
                if (s == "")
                {
                    continue;
                }
                // Deserializes cubes
                cube = JsonConvert.DeserializeObject<Cube>(s);
                // if a cube's mass is zero, it is removed from the dictionary and food is decremented
                if (cube.Mass == 0 && cube.food)
                {
                    foodCubes.Remove(cube.uid);
                    foodCount--;
                }
                else if (foodCubes.ContainsKey(cube.uid))
                    foodCubes[cube.uid] = cube;
                else
                    foodCubes.Add(cube.uid, cube);
                if (cube.food && cube.Mass != 0)
                {
                    foodCount += 1;
                }

            }

        }


    }
}