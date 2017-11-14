using System;
using Microsoft.VisualStudio.TestTools.UnitTesting;
using AgCubio;

namespace AgCubioTests
{

    /// <summary>
    /// Tests for the manipulation of cube objects in the world
    /// </summary>
    [TestClass]
    public class CubeTests
    {
        /// <summary>
        /// Tests the update function of the World class
        /// </summary>
        [TestMethod]
        public void TestWorldUpdate()
        {
            String[] s = { "{ 'loc_x':926.0,'loc_y':682.0,'argb_color':-65536,'uid':5571,'food':false,'Name':'3500 is love','Mass':1000.0}", "{ 'loc_x':748.0,'loc_y':364.0,'argb_color':-1845167,'uid':13,'food':true,'Name':'','Mass':1.0}" };
            World world = new World();
            world.update(s);
            Assert.IsTrue(world.foodCubes.ContainsKey(5571));
            String[] s1 = { "", "{ 'loc_x':926.0,'loc_y':682.0,'argb_color':-65536,'uid':5571,'food':false,'Name':'3500 is love','Mass':0.0}", "{ 'loc_x':926.0,'loc_y':682.0,'argb_color':-65536,'uid':13,'food':false,'Name':'3500 is love','Mass':100.0}" };
            world.update(s1);
        }

        /// <summary>
        /// Tests the constructor for a Cube object
        /// </summary>
        [TestMethod]
        public void TestCubeConstructor()
        {
            Cube cube = new Cube(555, 224.6, -655887, 45, 0, true, "Name", 4400.0);
            Assert.IsTrue(cube.food);
            Assert.AreEqual(cube.loc_x, 555);
            Assert.AreEqual(cube.loc_y, 224.6);
            Assert.AreEqual(cube.Mass, 4400.0);
            Assert.IsTrue(cube.Name.Equals("Name"));
            Assert.AreEqual(cube.argb_color, -655887);
            Assert.AreEqual(cube.team_id, 0);
            Assert.AreEqual(cube.uid, 45);
            Assert.AreEqual(cube.width, Math.Sqrt(4400.0));
        }

        /// <summary>
        /// Tests the addFood and addVirus methods
        /// </summary>
        [TestMethod]
        public void TestAddFoodVirus()
        {
            World world = new World();
            world.addFood(world.maxFood);
            Assert.AreEqual(5000, world.foodCount);
            world.addVirus(world.virusCount);
            Assert.AreEqual(15, world.virusCubes.Count);
        }

        /// <summary>
        /// Tests the setting of constants by the XML parameters file
        /// </summary>
        [TestMethod]
        public void TestConstants()
        {
            World world = new World();
            Assert.AreEqual(100, world.minSplitMass);
            Assert.AreEqual(16, world.maxSplitCubes);
            Assert.AreEqual(1.25, world.absorbConstant);
            Assert.AreEqual(60, world.heartBeatsPerSec);
            Assert.AreEqual(1000.0, world.playerStart);
        }

        /// <summary>
        /// Tests the attrition rate of cubes
        /// </summary>
        [TestMethod]
        public void TestAttrition()
        {
            Cube cube = new Cube(555, 224.6, -655887, 45, 0, true, "Name", 4400.0);
            World world = new World();
            world.AttritionPlayer(cube);
            Assert.AreEqual(4399.97522, cube.Mass, 1E-5);
        }

        /// <summary>
        /// Tests some of a cube's properties
        /// </summary>
        [TestMethod]
        public void TestCubeMembers()
        {
            Cube cube = new Cube(500, 200, -655887, 45, 0, true, "Name", 4000.0);
            World world = new World();
            Assert.AreEqual(63.245553, cube.width, 1E-5);
            Assert.AreEqual(15.811388, cube.collisionRadius, 1E-5);
            Assert.AreEqual(168.377223, cube.top, 1E-5);
            Assert.AreEqual(231.622776, cube.bottom, 1E-5);
            Assert.AreEqual(468.377223, cube.left, 1E-5);
            Assert.AreEqual(531.6227766, cube.right, 1E-5);
        }
    }
}
