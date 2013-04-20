package net.minecraft.ssMineShipMOD;

public class posXYZID {
	public int x;
	public int y;
	public int z;
	public int id;
	public int metaid;

	public posXYZID(int x,int y,int z,int id,int metaid)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.id = id;
		this.metaid = metaid;
	}
	
	public boolean equals(Object obj) {
        if( obj != null && obj instanceof posXYZID ){
        	posXYZID pos = (posXYZID)obj;
        	if(pos.x == this.x&&pos.y == this.y&&pos.z == this.z&&pos.id == this.id&&pos.metaid == this.metaid)
    			return true;
        }
        return false;
    }
	
	@Override
	public int hashCode()
	{
		return z*1000+y*100+x;
	}
	
}
