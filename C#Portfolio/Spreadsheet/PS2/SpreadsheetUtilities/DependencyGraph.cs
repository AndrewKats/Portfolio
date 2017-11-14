// Skeleton implementation written by Joe Zachary for CS 3500, September 2013.
// Version 1.1 (Fixed error in comment for RemoveDependency.)

//name: William Garnes
//uid:  u0922152

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace SpreadsheetUtilities
{

    /// <summary>
    /// (s1,t1) is an ordered pair of strings
    /// s1 depends on t1 --> t1 must be evaluated before s1
    /// 
    /// A DependencyGraph can be modeled as a set of ordered pairs of strings.  Two ordered pairs
    /// (s1,t1) and (s2,t2) are considered equal if and only if s1 equals s2 and t1 equals t2.
    /// (Recall that sets never contain duplicates.  If an attempt is made to add an element to a 
    /// set, and the element is already in the set, the set remains unchanged.)
    /// 
    /// Given a DependencyGraph DG:
    /// 
    ///    (1) If s is a string, the set of all strings t such that (s,t) is in DG is called dependents(s).
    ///        
    ///    (2) If s is a string, the set of all strings t such that (t,s) is in DG is called dependees(s).
    //
    // For example, suppose DG = {("a", "b"), ("a", "c"), ("b", "d"), ("d", "d")}
    //     dependents("a") = {"b", "c"}
    //     dependents("b") = {"d"}
    //     dependents("c") = {}
    //     dependents("d") = {"d"}
    //     dependees("a") = {}
    //     dependees("b") = {"a"}
    //     dependees("c") = {"a"}
    //     dependees("d") = {"b", "d"}
    /// </summary>
    public class DependencyGraph
    {
        private Dictionary<string, HashSet<string>> dependents { get; set; }
        private Dictionary<string, HashSet<string>> dependees { get; set; }
        
        /// <summary>
        /// Creates an empty DependencyGraph.
        /// Dependency graph will store added values into two separate dictionaries, one
        /// for dependents and one for dependees.  The second value will be stored into a
        /// hash set that is inside of the dictionary.
        /// </summary>
        public DependencyGraph()
        {
            dependents = new Dictionary<string, HashSet<string>>();
            dependees = new Dictionary<string,HashSet<string>>();
            Length = 0;
        }

        /// <summary>
        /// The number of ordered pairs in the DependencyGraph.
        /// </summary>
        public int Size
        {
            get{return (int)Length;}
        }

        private double Length { get; set; }

        /// <summary>
        /// The size of dependees(s).
        /// This property is an example of an indexer.  If dg is a DependencyGraph, you would
        /// invoke it like this:
        /// dg["a"]
        /// It should return the size of dependees("a")
        /// </summary>
        public int this[string s]
        {
            get 
            {
                if (dependees.ContainsKey(s))
                    return dependees[s].Count;
                return 0;
            }
        }

        /// <summary>
        /// Reports whether dependents(s) is non-empty.
        /// </summary>
        public bool HasDependents(string s) { return dependents.ContainsKey(s); }
            
        /// <summary>
        /// Reports whether dependees(s) is non-empty.
        /// </summary>
        public bool HasDependees(string s) { return dependees.ContainsKey(s); }

        /// <summary>
        /// Enumerates dependents(s).
        /// </summary>
        public IEnumerable<string> GetDependents(string s)
        {
            if (dependents.ContainsKey(s))
                return new HashSet<string>(dependents[s]);
            return new HashSet<string>();
        }

        /// <summary>
        /// Enumerates dependees(s).
        /// </summary>
        public IEnumerable<string> GetDependees(string s)
        {
            if (dependees.ContainsKey(s))
                return new HashSet<string>(dependees[s]);                
            return new HashSet<string>();
        }

        /// <summary>
        /// <para>Adds the ordered pair (s,t), if it doesn't exist</para>
        /// 
        /// <para>This should be thought of as:</para>   
        /// 
        ///   s depends on t
        ///
        /// </summary>
        /// <param name="s"> s cannot be evaluated until t is</param>
        /// <param name="t"> t must be evaluated first.  S depends on T</param>
        public void AddDependency(string s, string t)
        {
            AddDependency(s, t, dependents);
            AddDependency(t, s, dependees);
        }

        /// <summary>
        /// adds the ordered pair (s,t) to a specified dictionary
        /// </summary>
        /// <param name="s">the first value of the ordered pair</param>
        /// <param name="t">will be added to a hash set of other strings that are paired with s</param>
        /// <param name="dictionary">the dictionary the ordered pair will be added to</param>
        private void AddDependency(string s, string t, Dictionary<string, HashSet<string>> dictionary) 
        {
            if (dictionary.ContainsKey(s))
            {
                if (dictionary[s].Add(t))
                    Length += .5;
            }
                
            else
            {
                dictionary.Add(s, new HashSet<string>(new string[] { t }));
                Length += .5;
            }
                    
        }

        /// <summary>
        /// Removes the ordered pair (s,t), if it exists
        /// </summary>
        /// <param name="s">the first value of ordered pair</param>
        /// <param name="t">the second value of the ordered pair</param>
        public void RemoveDependency(string s, string t)
        {
            RemoveDependency(s, t, dependents);
            RemoveDependency(t, s, dependees);
        }

        /// <summary>
        /// removes the ordered pair (s,t) from a specified dictionary
        /// </summary>
        /// <param name="s">the first value of ordered pair</param>
        /// <param name="t">will be removed from a hash set strings that are paired with s</param>
        /// <param name="dictionary">the dictionary the ordered pair will be removed from</param>
        private void RemoveDependency(string s, string t, Dictionary<string,HashSet<string>> dictionary)
        {
            if (dictionary.ContainsKey(s))
            {
                if (dictionary[s].Remove(t))
                    Length -= .5;
                if (dictionary[s].Count == 0)
                    dictionary.Remove(s);
            }
        }

        /// <summary>
        /// Removes all existing ordered pairs of the form (s,r).  Then, for each
        /// t in newDependents, adds the ordered pair (s,t).
        /// </summary>
        public void ReplaceDependents(string s, IEnumerable<string> newDependents)  {Replace(s, newDependents, dependents, dependees);}

        /// <summary>
        /// Removes all existing ordered pairs of the form (r,s).  Then, for each 
        /// t in newDependees, adds the ordered pair (t,s).
        /// </summary>
        public void ReplaceDependees(string s, IEnumerable<string> newDependees) { Replace(s, newDependees, dependees, dependents); }

        /// <summary>
        /// clears out the hash table of s from a dictionary and adds new vaules
        /// </summary>
        /// <param name="s">variables whose hash set you want cleared</param>
        /// <param name="replacements">the new variables to be placed in the hash set for s</param>
        /// <param name="dictionary1">dictionary that holds the hash table for s</param>
        /// <param name="dictionary2">dictionary which will put s into the hash set of the replacements</param>
        private void Replace(string s, IEnumerable<string> replacements, Dictionary<string, HashSet<string>> dictionary1, Dictionary<string, HashSet<string>> dictionary2)
        {
            if (dictionary1.ContainsKey(s))
            {
                foreach (string t in dictionary1[s])
                {
                    RemoveDependency(t, s, dictionary2);
                    Length -= .5;
                }
                    
                dictionary1[s].Clear();
            }

            foreach (string t in replacements)
            {
                AddDependency(s, t, dictionary1);
                AddDependency(t, s, dictionary2);
            }
        }
    }
}