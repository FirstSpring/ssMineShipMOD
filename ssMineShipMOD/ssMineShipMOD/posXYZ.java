package net.minecraft.ssMineShipMOD;

public class posXYZ {
	public int x;
	public int y;
	public int z;

	public posXYZ(int x,int y,int z)
	{
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public boolean equals(Object obj) {
        if( obj != null && obj instanceof posXYZ ){
        	posXYZ pos = (posXYZ)obj;
        	if(pos.x == this.x&&pos.y == this.y&&pos.z == this.z)
    			return true;
        }
        return false;
    }
	
	@Override
	public int hashCode()
	{
		return x*1000+y*100+z;
	}
	
}
