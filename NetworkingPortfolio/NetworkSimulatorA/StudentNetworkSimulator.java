import java.util.ArrayList;

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
	
	
	ArrayList<Long> times = new ArrayList<Long>();
	long currentTime;
	
	boolean inTransit;
	
	int aSeq;
	Message currentMessage;
	int bAck;
	
	int timeoutCount;
	int originalTransmitted;
	int retransmissions;
	int corruptedAcks;
	int successfulAcks;
	int droppedPackets;
	
	int corruptedReceived;
	int acksSent;
	int packetsReceived;
	int bitFlag;
	
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
    	if(!inTransit)
    	{
	    	System.out.println();
	    	//System.out.println("Sent: " + message.getData());
	    	//System.out.println("aSeq: " + aSeq);
	    	
	    	currentTime = System.currentTimeMillis();
	    	
	    	currentMessage = message;
	    	int checkSum = message.getData().hashCode();
	    	Packet packet = new Packet(aSeq, 0, checkSum, message.getData());
	    	
	    	toLayer3(0, packet);
	    	startTimer(0, 20);
	    	
	    	inTransit = true;
	    	
	    	originalTransmitted++;
	    	//System.out.println("Transmitted: " + originalTransmitted);
    	}
    	else
    	{
    		droppedPackets++;
    		//System.out.println("Packet dropped");
    	}
    }
    
    // This routine will be called whenever a packet sent from the B-side 
    // (i.e. as a result of a toLayer3() being done by a B-side procedure)
    // arrives at the A-side.  "packet" is the (possibly corrupted) packet
    // sent from the B-side.
    protected void aInput(Packet packet)
    {
    	//System.out.println("ACK?");
    	String message = packet.getPayload();
    	String ackDummy = "ACK";
    	
    	/*
    	if((aSeq%2) != (packet.getAcknum()%2))
    	{
    		System.out.println("ERROR 0");
    		return;
    	}
    	*/
    	
    	
    	if(!message.equals(ackDummy))
    	{
    		corruptedAcks++;
    		//System.out.println("Message error: " + message);
    		//System.out.println("ERROR 1");
    		return;
    	}
    	
    	int checksum = ackDummy.hashCode();
    	
    	if(checksum != packet.getChecksum())
    	{
    		corruptedAcks++;
    		//System.out.println("aSeq error: " + aSeq);
    		//System.out.println("bAck error: " + bAck);
    		//System.out.println(checksum);
    		//System.out.println(packet.getChecksum());
    		//System.out.println("ERROR 2");
    		return;
    	}
    	
    	successfulAcks++;
    	inTransit = false;
    	
    	//System.out.println("GOT ACK");
    	
    	long RTT = System.currentTimeMillis() - currentTime;
    	
    	times.add(RTT);
    	
    	long timesum = 0;
    	for(long t : times)
    	{
    		timesum += t;
    	}
    	double timeAverage = ((double)timesum)/(times.size());
    	
    	
    	stopTimer(0);
    	aSeq++;
    	
    	System.out.println("Original transmissions: " + originalTransmitted);
    	System.out.println("ACKs sent: " + acksSent);
    	System.out.println("Retransmissions: " + retransmissions);
    	System.out.println("Average RTT: " + timeAverage);
    	
    	
    	System.out.println("Corrupted messages: " + corruptedReceived);
    	System.out.println("Corrupted ACKs: " + corruptedAcks);
    	System.out.println("Dropped packets: " + droppedPackets);
    	
    	
    	System.out.println("Corrupted ratio: " + (double)(corruptedReceived+corruptedAcks)/(double)(originalTransmitted+acksSent));

    	int lost = timeoutCount - corruptedReceived - corruptedAcks - droppedPackets;
    	System.out.println("Lost ratio: " + (double)lost/(double)(originalTransmitted+acksSent));
    	
    	
    	
    }
    
    // This routine will be called when A's timer expires (thus generating a 
    // timer interrupt). You'll probably want to use this routine to control 
    // the retransmission of packets. See startTimer() and stopTimer(), above,
    // for how the timer is started and stopped. 
    protected void aTimerInterrupt()
    {
    	timeoutCount++;
    	retransmissions++;
    	inTransit = false;
    	
    	//System.out.println("TIMEOUT");
    	//aOutput(currentMessage);
    	
    	if(!inTransit)
    	{
    		currentTime = System.currentTimeMillis();
    		
    		//System.out.println();
    		//System.out.println("Retransmit");
	    	//System.out.println();
	    	//System.out.println("Sent: " + currentMessage.getData());
	    	//System.out.println("aSeq: " + aSeq);
	    	
	    	int checkSum = currentMessage.getData().hashCode();
	    	Packet packet = new Packet(aSeq, 0, checkSum, currentMessage.getData());
	    	
	    	toLayer3(0, packet);
	    	startTimer(0, 20);
	    	
	    	inTransit = true;
	    	
	    	//originalTransmitted++;
	    	//System.out.println("Transmitted: " + originalTransmitted);
    	}
    }
    
    // This routine will be called once, before any of your other A-side 
    // routines are called. It can be used to do any required
    // initialization (e.g. of member variables you add to control the state
    // of entity A).
    protected void aInit()
    {
    	aSeq = 0;
    	currentMessage = new Message("");
    	
    	timeoutCount = 0;
    	originalTransmitted = 0;
    	retransmissions = 0;
    	corruptedAcks = 0;
    	successfulAcks = 0;
    	inTransit = false;
    }
    
    // This routine will be called whenever a packet sent from the B-side 
    // (i.e. as a result of a toLayer3() being done by an A-side procedure)
    // arrives at the B-side.  "packet" is the (possibly corrupted) packet
    // sent from the A-side.
    protected void bInput(Packet packet)
    {
    	packetsReceived++;
    	
    	String ackDummy = "ACK";
    	
    	//System.out.println("Received: " + packet.getPayload());
    	
    	String message = packet.getPayload();
    	int checksum = message.hashCode();
    	
    	if(checksum != packet.getChecksum())
    	{
    		corruptedReceived++;
    		
    		//System.out.println(checksum);
    		//System.out.println(packet.getChecksum());
    		//System.out.println("ERROR 3");
    		return;
    	}
    	
    	if(bitFlag != (packet.getSeqnum()%2))
    	{
    		//System.out.println("ERROR 0.1");
    		checksum =  ackDummy.hashCode();
    		Packet ack = new Packet(0, bAck, checksum, ackDummy);
    		toLayer3(1, ack);
    		return;
    	}
    	
    	if(bitFlag == 0)
    	{
    		bitFlag = 1;
    	}
    	else
    	{
    		bitFlag = 0;
    	}
    	
    	
    	
    	int newCheckSum = ackDummy.hashCode();
    	Packet newPacket = new Packet(0, bAck, newCheckSum, ackDummy);
    	
    	acksSent++;
    	
    	toLayer5(1, packet.getPayload());
    	toLayer3(1, newPacket);
    	
    	//System.out.println("Received successfully: " + packet.getPayload());
    	//System.out.println("bAck: " + bAck);
    	
    	bAck++;
    }
    
    // This routine will be called once, before any of your other B-side 
    // routines are called. It can be used to do any required
    // initialization (e.g. of member variables you add to control the state
    // of entity B).
    protected void bInit()
    {
    	bAck = 0;
    	corruptedReceived = 0;
    	acksSent = 0;
    	packetsReceived = 0;
    	bitFlag = 0;
    }
}


