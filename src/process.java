class process
{
    static int idAuto=1;
    private int id;
    private long size;
    private long timeOut;
    public memBlock memBlocks;
    process next;
    public process(long size, long timeOut)
    {
        id=idAuto++;
        this.size=size;
        this.timeOut=timeOut;

    }

    public int getId() {
        return id;
    }

    public long getSize() {
        return size;
    }

    public long getTimeOut() {
        return timeOut;
    }

    public void setTimeOut(long timeOut) {
        this.timeOut = timeOut;
    }

    public void setMemBlocks( long size,long startAddress) {
        memBlocks = new memBlock(size, startAddress);                           // changed
    }


    public memBlock getMemBlocks() {
        return memBlocks;
    }
    
    @Override
    public String toString()
    {
        return "The process data is :\n "+" ID: "+this.getId()+"\n"+"Size:  "+this.getSize()+" \n"+"time: "+this.getTimeOut()+"\n";
    }
}