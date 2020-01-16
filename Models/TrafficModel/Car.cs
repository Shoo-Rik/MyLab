using System;
using TrafficModel.Interfaces;

namespace TrafficModel
{
    public class Car : IValidate
    {
        public long MaxSpeed { get; }
        public long Speed { get; set; }
        public long Acceleration { get; set; }
        //public CarState State { get; set; }

        public Car(long speed, long maxSpeed, long acceleration)
        {
            Speed = speed;
            MaxSpeed = maxSpeed;
            Acceleration = acceleration;

            if (!IsValid())
            {
                throw new ArgumentOutOfRangeException();
            }
        }
        public bool IsValid()
        {
            return (Speed >= 0) && (Speed <= MaxSpeed);
        }
    }
}
