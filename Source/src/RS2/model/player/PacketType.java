package RS2.model.player;


	
public interface PacketType {
	public void processPacket(Client c, int packetType, int packetSize);
}

