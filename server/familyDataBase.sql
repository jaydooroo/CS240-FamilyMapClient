BEGIN TRANSACTION;
CREATE TABLE IF NOT EXISTS "User" (
	"username"	TEXT NOT NULL,
	"password1"	TEXT NOT NULL,
	"email"	Text NOT NULL,
	"firstName"	Text NOT NULL,
	"lastName"	Text NOT NULL,
	"gender"	VARCHAR(9) CHECK("gender" IN ('m', 'f')),
	"personID"	Text NOT NULL
);
CREATE TABLE IF NOT EXISTS "Person" (
	"personID"	Text NOT NULL,
	"associatedUsername"	Text NOT NULL,
	"firstName"	Text NOT NULL,
	"lastName"	Text NOT NULL,
	"gender"	VARCHAR(9) CHECK("gender" IN ('m', 'f')),
	"fatherID"	Text,
	"motherID"	Text,
	"spouseID"	Text
);
CREATE TABLE IF NOT EXISTS "Event" (
	"eventID"	Text NOT NULL,
	"associatedUsername"	Text NOT NULL,
	"PersonID"	Text NOT NULL,
	"latitude"	real,
	"longitude"	real,
	"country"	Text NOT NULL,
	"city"	Text NOT NULL,
	"eventType"	Text NOT NULL,
	"year"	Int
);
CREATE TABLE IF NOT EXISTS "Authtoken" (
	"authtoken"	Text NOT NULL,
	"username"	Text NOT NULL
);
COMMIT;
