using System;
using System.Collections.Generic;
using System.Linq;
using System.Net;
using System.Net.Sockets;
using System.Text;
using System.Threading.Tasks;

namespace AgCubio
{
    /// <summary>
    /// The current state of connection
    /// </summary>
    public class PreservedState
    {
        /// <summary>
        /// The socket of this state
        /// </summary>
        public Socket stateSocket = null;

        /// <summary>
        /// The size of the byte buffer
        /// </summary>
        public const int BufferSize = 1024;

        /// <summary>
        /// Holds the bytes sent by the server
        /// </summary>
        public byte[] buffer = new byte[BufferSize];

        /// <summary>
        /// Stores the json sent by the server
        /// </summary>
        public StringBuilder sb = new StringBuilder();

        /// <summary>
        /// The current callback function of the state
        /// </summary>
        public Action<PreservedState> Callback;

        /// <summary>
        /// True if a connection error has occured
        /// </summary>
        public bool error;
    }

    /// <summary>
    /// Handles connection of our client to the server
    /// </summary>
    public class Client
    {
        /// <summary>
        /// The port we're using
        /// </summary>
        private const int port = 11000;

        /// <summary>
        /// The response of the client
        /// </summary>
        private static string response = "";

        /// <summary>
        /// Creates a new socket and begins connect
        /// </summary>
        /// <param name="callbackfunction"> Our callback function</param>
        /// <param name="hostname"> The hostname of the server</param>
        /// <returns></returns>
        public static PreservedState Connect_to_Server(Action<PreservedState> callbackfunction, string hostname)
        {
            PreservedState currentState = new PreservedState();
            currentState.stateSocket = new Socket(AddressFamily.InterNetwork, SocketType.Stream, ProtocolType.Tcp);

            // Set the callback
            currentState.Callback = callbackfunction;

            // Begin the connect
            currentState.stateSocket.BeginConnect(hostname, 11000, Connected_to_Server, currentState);

            return currentState;
        }

        /// <summary>
        /// The callback of Connect_to_Server which ends the connect
        /// </summary>
        /// <param name="state_in_an_ar_object"> The state passed from the caller</param>
        public static void Connected_to_Server(IAsyncResult state_in_an_ar_object)
        {
            PreservedState currentState = (PreservedState)state_in_an_ar_object.AsyncState;
            try
            {
                // End the connect
                currentState.stateSocket.EndConnect(state_in_an_ar_object);

                // Call the callback
                currentState.Callback(currentState);
            }
            catch (Exception e)
            {
                // Tell the GUI there's an error
                currentState.error = true;
                currentState.Callback(currentState);

                Console.WriteLine(e.Message);
            }
        }

        /// <summary>
        /// The callback of i_want_more_data that ends the receive
        /// </summary>
        /// <param name="state_in_an_ar_object"> The state sent from the caller</param>
        public static void ReceiveCallback(IAsyncResult state_in_an_ar_object)
        {
            PreservedState state = (PreservedState)state_in_an_ar_object.AsyncState;
            try
            {
                Socket sock = state.stateSocket;

                // End the receive
                int bytesRead = sock.EndReceive(state_in_an_ar_object);

                // Put the server's bytes into the stringbuilder
                if (bytesRead > 0)
                {
                    lock (state.sb)
                    {
                        state.sb.Append(Encoding.UTF8.GetString(state.buffer, 0, bytesRead));
                        // Call the callback
                        state.Callback(state);
                    }
                }
                else
                {
                    if (state.sb.Length > 1)
                    {
                        response = state.sb.ToString();
                    }
                }
            }
            catch (Exception e)
            {
                // Tell the GUI there's an error
                state.error = true;
                state.Callback(state);
                Console.WriteLine(e.Message);
            }
        }

        /// <summary>
        /// Begins receive for more data from the server
        /// </summary>
        /// <param name="state_in_an_ar_object"> The state sent from the caller</param>
        public static void i_want_more_data(PreservedState state_in_an_ar_object)
        {
            PreservedState state = (PreservedState)state_in_an_ar_object;
            try
            {
                Socket client = state.stateSocket;
                // Begin the receive
                client.BeginReceive(state.buffer, 0, PreservedState.BufferSize, 0, ReceiveCallback, state);
            }
            catch (Exception e)
            {
                // Tell the GUI there's an error
                state.error = true;
                state.Callback(state);
                Console.WriteLine(e.Message);
            }
        }

        /// <summary>
        /// Begins the send to the server
        /// </summary>
        /// <param name="state"></param>
        /// <param name="data"></param>
        public static void Send(PreservedState state, String data)
        {
            try
            {
                // Encode the string to send
                byte[] byteData = Encoding.UTF8.GetBytes(data);
                Socket socket = state.stateSocket;
                // Begin sending data to the server
                socket.BeginSend(byteData, 0, byteData.Length, 0, SendCallBack, socket);
            }
            catch (Exception e)
            {
                state.error = true;
                Console.WriteLine(e.Message);
            }
        }

        /// <summary>
        /// The callback of Send that ends the send
        /// </summary>
        /// <param name="state_in_an_ar_object"> The state sent from the caller</param>
        public static void SendCallBack(IAsyncResult state_in_an_ar_object)
        {
            try
            {
                Socket sock = (Socket)state_in_an_ar_object.AsyncState;
                // End sending data to the server
                sock.EndSend(state_in_an_ar_object);
            }
            catch (Exception e)
            {
                Console.WriteLine(e.Message);
            }
        }

    }
}
