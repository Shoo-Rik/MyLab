using System;
using System.Collections.Generic;
using System.Linq;
using TrafficModel.Interfaces;

namespace TrafficModel
{
    public class RoadWayLocationProcessor : IRoadWayLocationProcessor
    {
        public IRoadWayLocation GetNextRoadLocationAfterTick(IRoadWayLocation currentRoadWayLocation)
        {
            IRoadWayLocation nextRoadWayLocation = new RoadWayLocation(currentRoadWayLocation.RoadWay, new Dictionary<long, Car>());

            IDictionary<long, Car> currentCars = currentRoadWayLocation.Cars;

            // [TODO]: Process car to be out from the Road 
            long forwardCarFreeDistance = currentRoadWayLocation.RoadWay.Distance + 1;
            CarContext forwardCarContext = new CarContext(currentRoadWayLocation.RoadWay, forwardCarFreeDistance); 

            foreach (var item in currentCars.OrderByDescending(c => c.Key))
            {
                RoadWay roadWay = currentRoadWayLocation.RoadWay;
                long currentDistance = item.Key;

                Car car = item.Value;
                var currentCarContext = new CarContext(roadWay, currentDistance);

                long nextCarDistance = GetNextCarDistanceAfterTick(car, currentCarContext, forwardCarContext);

                if (nextCarDistance > currentRoadWayLocation.RoadWay.Distance)
                {
                    // [TODO]: When car is out of the Road
                    continue;
                }

                if (nextCarDistance == currentDistance)
                {
                    // [TODO]: When car does not move
                }

                nextRoadWayLocation.Cars.Add(nextCarDistance, car);

                forwardCarContext = currentCarContext;
            }

            return nextRoadWayLocation;
        }

        public long GetNextCarDistanceAfterTick(Car car, CarContext currentCarContext, CarContext forwardCarContext)
        {
            long availableInterval = forwardCarContext.RoadWayDistance - currentCarContext.RoadWayDistance - 1;
            if (availableInterval <= 0)
            {
                // [TODO]
            }

            long result;
            if (car.Speed < availableInterval)
            {
                long maxAcceleration = Math.Min(car.MaxSpeed - car.Speed, );

                if (car.Speed + car.Acceleration + 1 <= availableInterval)
                {
                    car.Acceleration += 1;
                    car.Speed += car.Acceleration;
                    result = car.Speed;
                }
                else
                {
                    car.Acceleration = availableInterval - car.Speed;
                }
            }
            else
            {

            }

            // [TODO]
            return -1;
        }
    }
}