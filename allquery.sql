/*Final Project CSCI3091 Covid-19 Contact-Tracer*/
/*BannerID: B00870489*/

use lad;
/*query to create table storing mobile devices hash values.*/
create table MobileDevice(HashValue varchar(255) primary key);

/*Creates TestResult table to store TeshHash of tests and its results.*/
create table TestResult(
	TestHash varchar(100) primary key,
    Days int,
    Result boolean);

/*creates table Device_Result which connects two tables MobileDevice and TestResult.*/
create table Device_Result(
	mdHash varchar(100) not null,
    tHash varchar(100) not null unique,
    constraint primary key (mdHash, tHash),
    constraint foreign_key foreign key (mdHash) references MobileDevice(HashValue),
    constraint foreign_key1 foreign key (tHash) references TestResult(TestHash));
 
 /*creates table ContactInfo which stores all the contact details of individuals and their positive test hash.*/
create table ContactInfo(
	contactID int primary key auto_increment not null,
    mdHash varchar(255) not null,
    cdHash varchar(255),
    Days int,
    Duration int,
    Notify boolean,
    constraint foreign_key2 foreign key (mdHash) references MobileDevice(HashValue),
    constraint foreign_key3 foreign key (cdHash) references MobileDevice(HashValue)
    );
    