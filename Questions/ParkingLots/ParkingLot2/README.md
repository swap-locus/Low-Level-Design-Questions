#### Introduction
Parking Lot is a system where we can park our vehicles and get them out when we want by paying some amount in exchange of parking our vehicle on hourly basis.

#### Requirements of Parking Lot
1. Parking Lot should have an Id, multiple parking floors, multiple parking slots on each floor.
2. Each Parking lot has multiple entrance where entry is recorded, and multiple exit points where payment is done.
3. Each parking slot has types which can be: TWO_WHEELER, FOUR_WHEELER, LARGE, ABLED etc.
4. Each vehicle is given a ticket at time of entry with entry time mentioned on it.
5. System should have user with roles: ADMIN, MANAGER, SIMPLE_USER etc.
6. user should have their personal details, basic details, role, contact details etc.
7. ADMIN or manager only can add or remove floors/entry/exist/slots in the parking lot.
8. Only ADMIN can add a new Parking Lot.
9. ADMIN can add manager, or an existing manager can add a new manager.
10. ADMIN or manager can add a new cashier or security.
11. SIMPLE_USER should issue ticket at time of entry and collect payment at time of exit.
12. Payment can be done in cash, or card, or UPI.
13. The charges should be apply on Hourly basis.
14. The charges should be different for different types of parking spots.

#### Entities & Enums
1. ParkingRelated
    1. ParkingLot
    2. ParkingFloor
    3. ParkingSpot
    4. ParkingEntryPoint
    5. ParkingExitPoint
    6. ParkingTicket
    7. Different parkingSpotType classes
        1. TwoWheelerVehicleParkingSpot
        2. FourWheelerVehicleParkingSpot
        3. LargeVehicleParkingSpot
        4. AbledVehicleParkingSpot

2. User Related
    1. User
    2. Contact
    3. Address

3. Common
    1. EntityDefinition: id, createAt, updatedAt

4. PaymentRelated
    1. Payment
    2. CostCalculation
        1. HourlyBasedCalculation

5. Vehicle
    1. Vehicle
    2. Different VehicleTypeClasses
        1. TwoWheelerVehicle
        2. FourWheelerVehicle
        3. LargeVehicle
        4. AbledVehicle

6. enums
    1. ParkingSpotType
    2. TicketStatus
    3. UserRole
    4. PaymentStatus
    5. VehicleType

#### Diagrams
See [UML.md](./UML.md) for the full class diagram (Mermaid + ASCII), the three-layer architecture overview, and a ParkingLot1 vs ParkingLot2 comparison.
