namespace TrafficModel.Interfaces
{
    public interface IRoadWayLocationProcessor
    {
        IRoadWayLocation GetNextRoadLocationAfterTick(IRoadWayLocation currentRoadLocation);
    }
}