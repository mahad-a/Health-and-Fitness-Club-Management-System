CREATE TABLE IF NOT EXISTS Members (
    memberID SERIAL PRIMARY KEY,
	first_name VARCHAR(100) NOT NULL,
	last_name VARCHAR(100) NOT NULL,
    username VARCHAR(100) NOT NULL,
	passkey VARCHAR(100) NOT NULL,
	fitness_goal VARCHAR(255),
    weight DECIMAL(5,2),
	enrollment_date DATE NOT NULL
);

CREATE TABLE IF NOT EXISTS Trainers (
    trainerID SERIAL PRIMARY KEY,
    username VARCHAR(100) NOT NULL,
    specialization VARCHAR(100),
    schedule TEXT,
	hire_date DATE NOT NULL,
    FOREIGN KEY (trainerID) REFERENCES Members(memberID)
);

CREATE TABLE IF NOT EXISTS Classes (
    classID SERIAL PRIMARY KEY,
    className VARCHAR(100) NOT NULL,
    trainerID INT,
    booking_date VARCHAR(20),
    time_block VARCHAR(20),
    class_list TEXT,
    FOREIGN KEY (trainerID) REFERENCES Trainers(trainerID)
);

CREATE TABLE IF NOT EXISTS RoomBookings (
    bookingID SERIAL PRIMARY KEY,
    roomNumber INT NOT NULL,
    booking_date VARCHAR(20),
    time_block VARCHAR(20),
    booked_by INT,
    FOREIGN KEY (booked_by) REFERENCES Members(memberID) 
);

CREATE TABLE IF NOT EXISTS Equipment (
    equipmentID SERIAL PRIMARY KEY,
    equipment_name VARCHAR(100) NOT NULL,
    quantity INT,
	last_maintenance_date DATE
);

CREATE TABLE IF NOT EXISTS TrainerAvailability (
    availabilityID SERIAL PRIMARY KEY,
    trainerID INT,
    available_day VARCHAR(20),
    FOREIGN KEY (trainerID) REFERENCES Trainers(trainerID)
);

CREATE TABLE IF NOT EXISTS Payment (
    paymentID SERIAL PRIMARY KEY,
    amount DECIMAL(10,2) NOT NULL,
    payment_date DATE,
    payment_type VARCHAR(50),
    member_id INT,
    FOREIGN KEY (member_id) REFERENCES Members(memberID)
);

CREATE TABLE IF NOT EXISTS ExerciseRoutines (
    routineID SERIAL PRIMARY KEY,
    memberID INT NOT NULL,
    routine_name VARCHAR(100) NOT NULL,
    duration INT,
	quantity INT,
    date_of_exercise DATE,
    FOREIGN KEY (memberID) REFERENCES Members(memberID)
);

CREATE TABLE IF NOT EXISTS FitnessAchievements (
    achievementID SERIAL PRIMARY KEY,
    memberID INT NOT NULL,
    achievement_info TEXT NOT NULL,
    achievement_date DATE,
    FOREIGN KEY (memberID) REFERENCES Members(memberID)
);

CREATE TABLE IF NOT EXISTS Days (
	day VARCHAR(20) PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS Admins (
	adminID SERIAL PRIMARY KEY,
	username VARCHAR(100) NOT NULL,
	FOREIGN KEY (adminID) REFERENCES Members(memberID)
);

CREATE TABLE IF NOT EXISTS HealthStatistics (
	healthID SERIAL PRIMARY KEY,
	memberID INT NOT NULL,
	stat TEXT,
	FOREIGN KEY (memberID) REFERENCES Members(memberID)
);
