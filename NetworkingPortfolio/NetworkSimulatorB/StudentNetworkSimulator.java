import java.util.ArrayList;

/*
 * Andrew Katsanevas
 * 3/27/2017
 * CS 4480
 * PA2B
 */
public class StudentNetworkSimulator extends NetworkSimulator
{
    /*
     * Predefined Constants (static member variables):
     *
     *   int MAXDATASIZE : the maximum size of the Message data and
     *                     Packet payload
     *
     *   int A           : a predefined integer that represents entity A
     *   int B           : a predefined integer that represents entity B
     *
     *
     * Predefined Member Methods:
     *
     *  void stopTimer(int entity): 
     *       Stops the timer running at "entity" [A or B]
     *  void startTimer(int entity, double increment): 
     *       Starts a timer running at "entity" [A or B], which will expire in
     *       "increment" time units, causing the interrupt handler to be
     *       called.  You should only call this with A.
     *  void toLayer3(int callingEntity, Packet p)
     *       Puts the packet "p" into the network from "callingEntity" [A or B]
     *  void toLayer5(int entity, String dataSent)
     *       Passes "dataSent" up to layer 5 from "entity" [A or B]
     *  double getTime()
     *       Returns the current time in the simulator.  Might be useful for
     *       debugging.
     *  void printEventList()
     *       Prints the current event list to stdout.  Might be useful for
     *       debugging, but probably not.
     *
     *
     *  Predefined Classes:
     *
     *  Message: Used to encapsulate a message coming from layer 5
     *    Constructor:
     *      Message(String inputData): 
     *          creates a new Message containing "inputData"
     *    Methods:
     *      boolean setData(String inputData):
     *          sets an existing Message's data to "inputData"
     *          returns true on success, false otherwise
     *      String getData():
     *          returns the data contained in the message
     *  Packet: Used to encapsulate a packet
     *    Constructors:
     *      Packet (Packet p):
     *          creates a new Packet that is a copy of "p"
     *      Packet (int seq, int ack, int check, String newPayload)
     *          creates a new Packet with a sequence field of "seq", an
     *          ack field of "ack", a checksum field of "check", and a
     *          payload of "newPayload"
     *      Packet (int seq, int ack, int check)
     *          chreate a new Packet with a sequence field of "seq", an
     *          ack field of "ack", a checksum field of "check", and
     *          an empty payload
     *    Methods:
     *      boolean setSeqnum(int n)
     *          sets the Packet's sequence field to "n"
     *          returns true on success, false otherwise
     *      boolean setAcknum(int n)
     *          sets the Packet's ack field to "n"
     *          returns true on success, false otherwise
     *      boolean setChecksum(int n)
     *          sets the Packet's checksum to "n"
     *          returns true on success, false otherwise
     *      boolean setPayload(String newPayload)
     *          sets the Packet's payload to "newPayload"
     *          returns true on success, false otherwise
     *      int getSeqnum()
     *          returns the contents of the Packet's sequence field
     *      int getAcknum()
     *          returns the contents of the Packet's ack field
     *      int getChecksum()
     *          returns the checksum of the Packet
     *      int getPayload()
     *          returns the Packet's payload
     *
     */

    // Add any necessary class variables here.  Remember, you cannot use
    // these variables to send messages error free!  They can only hold
    // state information for A or B.
    // Also add any necessary methods (e.g. checksum of a String)
	
	
	int base;
	int nextseqnum;
	int windowSizeN = 8;
	ArrayList<Message> buffer;
	ArrayList<Packet> window;
	
	int corruptedAcks;
	int successfulAcks;
	int amountTransmitted;
	int amountRetransmitted;
	int layer5messages;
	int layer5sent;
	int totalBuffered;
	int timeouts;
	int acksReceived;
	long timeStarted;
	
	
	
	int packetsReceived;
	int corruptedReceived;
	int expectedSeqnum;
	int wrongSeqnums;
	int acksSent;
	
    // This is the constructor.  Don't touch!
    public StudentNetworkSimulator(int numMessages,
                                   double loss,
                                   double corrupt,
                                   double avgDelay,
                                   int trace,
                                   long seed)
    {
        super(numMessages, loss, corrupt, avgDelay, trace, seed);
    }

    // This routine will be called whenever the upper layer at the sender [A]
    // has a message to send.  It is the job of your protocol to insure that
    // the data in such a message is delivered in-order, and correctly, to
    // the receiving upper layer.
    protected void aOutput(Message message)
    {  	
    	printStats();
    	
    	layer5messages++;
    	
    	// Check if the message needs to be buffered
    	if(nextseqnum < base + windowSizeN)
    	{    		
    		// If nothing is in the buffer, send the message
    		if(buffer.isEmpty())
    		{
    			int checkSum = message.getData().hashCode() + nextseqnum;
    			Packet packet = new Packet(nextseqnum, 0, checkSum, message.getData());
    			
    			System.out.println();
            	System.out.println("Sent: " + message.getData() + " " + nextseqnum);
    			
    			window.add(packet);
        		if(base == nextseqnum)
        		{
        			startTimer(0, 100);
        		}
        		nextseqnum++;
        		toLayer3(0, packet);  
        		
        		layer5sent++;
    		}
    		// If there are messages in the buffer, add this message to the buffer and send a buffered message
    		else
    		{
    			buffer.add(message);
    			totalBuffered++;
    			
    			Message bufferMessage = buffer.remove(0);
    			int checkSum = bufferMessage.getData().hashCode() + nextseqnum;
    			Packet packet = new Packet(nextseqnum, 0, checkSum, bufferMessage.getData());
    			
    			System.out.println();
            	System.out.println("Sent: " + bufferMessage.getData() + " " + nextseqnum + " [from buffer]");
    			
    			window.add(packet);
        		if(base == nextseqnum)
        		{
        			startTimer(0, 100);
        		}
        		nextseqnum++;
        		toLayer3(0, packet);
        		
        		layer5sent++;
    		}
    		
    	}
    	// Buffer the message
    	else
    	{
    		buffer.add(message);
    		totalBuffered++;
    	}
    }
    
    // This routine will be called whenever a packet sent from the B-side 
    // (i.e. as a result of a toLayer3() being done by a B-side procedure)
    // arrives at the A-side.  "packet" is the (possibly corrupted) packet
    // sent from the B-side.
    protected void aInput(Packet packet)
    {
    	acksReceived++;
    	
    	System.out.println();
    	System.out.println("ACK? " + packet.getAcknum());
    	
    	String message = packet.getPayload();
    	String ackDummy = "ACK";
    	
    	// Check if the ack payload is corrupted
    	if(!message.equals(ackDummy))
    	{
    		System.out.println("Corrupted Ack payload");
    		corruptedAcks++;
    		return;
    	}
    	
    	// Check if any of the ack is corrupted
    	int checksum = ackDummy.hashCode() + packet.getAcknum();
    	if(checksum != packet.getChecksum())
    	{
    		System.out.println("Corrupted Ack");
    		corruptedAcks++;
    		return;
    	}
    	
    	successfulAcks++;
    	System.out.println("GOT ACK " + packet.getAcknum());
    	
    	// Change the base of the window
    	base = packet.getAcknum() + 1;
    	
    	// Reset the timer
    	if(base == nextseqnum)
    	{
    		stopTimer(0);
    	}
    }
    
    // This routine will be called when A's timer expires (thus generating a 
    // timer interrupt). You'll probably want to use this routine to control 
    // the retransmission of packets. See startTimer() and stopTimer(), above,
    // for how the timer is started and stopped. 
    protected void aTimerInterrupt()
    {
    	timeouts++;
    	
    	System.out.println();
    	System.out.println("TIMEOUT");
    	
    	// Resend the window
    	for(int i=base; i < nextseqnum; i++)
    	{
    		toLayer3(0, window.get(i));
    		
    		amountRetransmitted++;
    		System.out.println("Resent: " + window.get(i).getPayload() + " " + window.get(i).getSeqnum());
    	}
    	
    	// Restart the timer
    	startTimer(0, 100);
    }
    
    // This routine will be called once, before any of your other A-side 
    // routines are called. It can be used to do any required
    // initialization (e.g. of member variables you add to control the state
    // of entity A).
    protected void aInit()
    {
    	base = 0;
    	nextseqnum = 0;
    	buffer = new ArrayList<Message>();
    	window = new ArrayList<Packet>();
    	corruptedAcks = 0;
    	successfulAcks = 0;
    	amountTransmitted = 0;
    	amountRetransmitted = 0;
    	layer5messages = 0;
    	layer5sent = 0;
    	totalBuffered = 0;
    	timeouts = 0;
    	acksReceived = 0;
    	timeStarted = System.currentTimeMillis();
    }
    
    // This routine will be called whenever a packet sent from the B-side 
    // (i.e. as a result of a toLayer3() being done by an A-side procedure)
    // arrives at the B-side.  "packet" is the (possibly corrupted) packet
    // sent from the A-side.
    protected void bInput(Packet packet)
    {    	
    	packetsReceived++;
    	
    	String ackDummy = "ACK";
    	
    	System.out.println();
    	System.out.println("Received: " + packet.getPayload() + " " + packet.getSeqnum());
    	
    	String message = packet.getPayload();
    	
    	// Check for corruption
    	int checksum = packet.getSeqnum() + packet.getAcknum() + message.hashCode();
    	if(checksum != packet.getChecksum())
    	{
    		System.out.println("Corrupted: Expected " + checksum + " got " + packet.getChecksum());
    		corruptedReceived++;
    		return;
    	}
    	
    	
    	// Check that the seqnum is correct
    	if(expectedSeqnum != packet.getSeqnum())
    	{
    		System.out.println("Wrong seqnum, expected " + expectedSeqnum + ", got " + packet.getSeqnum());
    		wrongSeqnums++;
    		return;
    	}
    	
    	
    	// Create an ack packet
    	int ackChecksum = ackDummy.hashCode() + expectedSeqnum;
    	Packet ackPacket = new Packet(0, expectedSeqnum, ackChecksum, ackDummy);
    	
    	
    	// Pass the data to layer 5
    	toLayer5(1, packet.getPayload());
    	
    	// Send back the ack packet
    	toLayer3(1, ackPacket);
    	acksSent++;
    	
    	System.out.println("Received successfully: " + packet.getPayload() + " " + packet.getSeqnum());
    	//System.out.println("Expected Seqnum: " + expectedSeqnum);
    	
    	expectedSeqnum++;
    }
    
    // This routine will be called once, before any of your other B-side 
    // routines are called. It can be used to do any required
    // initialization (e.g. of member variables you add to control the state
    // of entity B).
    protected void bInit()
    {
    	expectedSeqnum = 0;
    	packetsReceived = 0;
    	corruptedReceived = 0;
    	wrongSeqnums = 0;
    	acksSent = 0;
    }
    
    /*
     * Print out statistics for this session
     */
    protected void printStats()
    {
    	System.out.println();
    	
    	System.out.println("==============================================");
    	System.out.println("STATS");
    	System.out.println("----------------------------------------------");
    	
    	
    	System.out.println("Messages from layer 5: " + layer5messages);
    	System.out.println("Messages sent: " + layer5sent);
    	System.out.println("Messages successfully received: " + successfulAcks);
    	int aPackets = layer5sent + amountRetransmitted;
    	System.out.println("Packets sent from A: " + aPackets);
    	System.out.println("ACKs sent from B: " + acksSent);
    	
    	
    	
    	long timeElapsed = System.currentTimeMillis() - timeStarted;
    	double avgRTT = (double)successfulAcks / (double)timeElapsed;
    	System.out.println("Average RTT: " + avgRTT + " ms");
    	
    	System.out.println("----------------------------------------------");
    	System.out.println("Timeouts: " + timeouts);
    	System.out.println("Retransmitted messages: " + amountRetransmitted);
    	
    	
    	
    	System.out.println("----------------------------------------------");
    	System.out.println("Corrupted messages: " + corruptedReceived);
    	System.out.println("Corrupted ACKs: " + corruptedAcks);
    	int corruptedTotal = corruptedReceived + corruptedAcks;
    	System.out.println("Corrupted total: " + corruptedTotal);
    	
    	
    	
    	double corruptedRatioAtoB = (double)(corruptedReceived) / (double)(layer5sent + amountRetransmitted);
    	double corruptedRatioBtoA = (double)(corruptedAcks) / (double)(acksSent);
    	double corruptedRatioTotal = (double)(corruptedReceived+corruptedAcks) / (double)(layer5sent + acksSent + amountRetransmitted);
    	
    	System.out.println("----------------------------------------------");
    	System.out.println("Corrupted ratio A to B: " + corruptedRatioAtoB);
    	System.out.println("Corrupted ratio B to A: " + corruptedRatioBtoA);
    	System.out.println("Corrupted ratio total: " + corruptedRatioTotal);
    	
    	
    	int lostA = layer5sent + amountRetransmitted - packetsReceived;
    	int lostB = acksSent - acksReceived;
    	int lostTotal = lostA+lostB;
    	System.out.println("----------------------------------------------");
    	System.out.println("Lost packets A to B: " + lostA);
    	System.out.println("Lost packets B to A: " + lostB);
    	System.out.println("Lost packets total: " + lostTotal);
    	
    	
    	System.out.println("----------------------------------------------");
    	System.out.println("Lost ratio A to B: " + (double)lostA / (double)(layer5sent + amountRetransmitted));
    	System.out.println("Lost ratio B to A: " + (double)lostB / (double)acksSent);
    	System.out.println("Lost ratio total: " + (double)(lostA+lostB) / (double)(layer5sent + amountRetransmitted + acksSent));
    	
    	System.out.println("==============================================");
    	
    	System.out.println();
    }
}


