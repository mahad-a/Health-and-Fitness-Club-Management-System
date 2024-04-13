INSERT INTO Days (day) VALUES 
('Monday'),
('Tuesday'),
('Wednesday'),
('Thursday'),
('Friday'),
('Saturday'),
('Sunday');

INSERT INTO RoomBookings (roomNumber, booking_date, time_block, booked_by, session)
VALUES 
    (1, 'MONDAY', 'Morning', NULL, NULL),
    (2, 'TUESDAY', 'Afternoon', NULL, NULL),
    (3, 'WEDNESDAY', 'Evening', NULL, NULL),
    (4, 'THURSDAY', 'Night', NULL, NULL),
    (5, 'FRIDAY', 'Morning', NULL, NULL),
    (6, 'SATURDAY', 'Afternoon', NULL, NULL),
    (7, 'SUNDAY', 'Evening', NULL, NULL),
    (8, 'MONDAY', 'Night', NULL, NULL),
    (9, 'TUESDAY', 'Morning', NULL, NULL),
    (10, 'WEDNESDAY', 'Afternoon', NULL, NULL);

TRUNCATE TABLE Payment;

INSERT INTO Admins (adminID, username)
VALUES
	(15, 'admin');
