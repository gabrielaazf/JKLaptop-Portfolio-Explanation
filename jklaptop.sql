-- phpMyAdmin SQL Dump
-- version 5.1.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jan 14, 2022 at 09:16 AM
-- Server version: 10.4.19-MariaDB
-- PHP Version: 8.0.7

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `jklaptop`
--

-- --------------------------------------------------------

--
-- Table structure for table `brand`
--

CREATE TABLE `brand` (
  `BrandID` char(5) NOT NULL,
  `BrandName` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `brand`
--

INSERT INTO `brand` (`BrandID`, `BrandName`) VALUES
('BD001', 'Asus'),
('BD003', 'HP'),
('BD004', 'Lenovo'),
('BD005', 'Samsung'),
('BR053', 'Apple'),
('BR067', 'Acer'),
('BR079', 'Vaio'),
('BR955', 'Huawei');

-- --------------------------------------------------------

--
-- Table structure for table `cart`
--

CREATE TABLE `cart` (
  `UserID` char(5) NOT NULL,
  `ProductID` char(5) NOT NULL,
  `Qty` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `detailtransaction`
--

CREATE TABLE `detailtransaction` (
  `TransactionID` char(5) NOT NULL,
  `ProductID` char(5) NOT NULL,
  `Qty` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `detailtransaction`
--

INSERT INTO `detailtransaction` (`TransactionID`, `ProductID`, `Qty`) VALUES
('TR074', 'PD005', 1),
('TR363', 'PD308', 1);

-- --------------------------------------------------------

--
-- Table structure for table `headertransaction`
--

CREATE TABLE `headertransaction` (
  `TransactionID` char(5) NOT NULL,
  `UserID` char(5) NOT NULL,
  `TransactionDate` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `headertransaction`
--

INSERT INTO `headertransaction` (`TransactionID`, `UserID`, `TransactionDate`) VALUES
('TR074', 'US949', '2022-01-14'),
('TR363', 'US949', '2022-01-14');

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `ProductID` char(5) NOT NULL,
  `BrandID` char(5) NOT NULL,
  `ProductName` varchar(100) NOT NULL,
  `ProductPrice` int(13) NOT NULL,
  `ProductStock` int(5) NOT NULL,
  `ProductRating` int(3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `product`
--

INSERT INTO `product` (`ProductID`, `BrandID`, `ProductName`, `ProductPrice`, `ProductStock`, `ProductRating`) VALUES
('PD001', 'BD001', 'ASUS VIVOBOOK X413JA', 7200000, 12, 8),
('PD002', 'BD001', 'ASUS L510MA N4020', 5600000, 11, 7),
('PD005', 'BD003', 'HP Pavilion Gaming Laptop 15-ec1071AX', 25000000, 15, 10),
('PD006', 'BD003', 'HP Pavilion Laptop 14-dv0068TX', 13500000, 13, 9),
('PD050', 'BD005', 'Samsung Laptop', 19000000, 8, 10),
('PD229', 'BR079', 'Sony Vaio', 12000000, 10, 10),
('PD308', 'BD004', 'Lenovo Yoga S70', 7800000, 10, 10),
('PD889', 'BR067', 'Acer Aspire', 6000000, 0, 8),
('PD918', 'BD001', 'Asus Zenbook', 12000000, 15, 10);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `UserID` char(5) NOT NULL,
  `UserName` varchar(50) NOT NULL,
  `UserEmail` varchar(50) NOT NULL,
  `UserPassword` varchar(25) NOT NULL,
  `UserGender` varchar(6) NOT NULL,
  `UserAddress` varchar(100) NOT NULL,
  `UserRole` varchar(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`UserID`, `UserName`, `UserEmail`, `UserPassword`, `UserGender`, `UserAddress`, `UserRole`) VALUES
('US001', 'abel_azzahra', 'GabrielaAzzahra@gmail.com', 'AbelCantik123', 'Female', 'Pattimura Kencana No. 25 Street', 'Member'),
('US002', 'admin', 'admin@gmail.com', 'Admin1234', 'Female', 'Pegangsaan Timur no 58 Street', 'Admin'),
('US319', 'jonis', 'jonis@gmail.com', 'jonis123', 'Male', 'Kebon Jeruk Street', 'Member'),
('US949', 'emilyindrakusuma', 'emilyindrakusuma@gmail.com', 'emily1234', 'Female', 'Surabaya Timur Street', 'Member');

--
-- Indexes for dumped tables
--

--
-- Indexes for table `brand`
--
ALTER TABLE `brand`
  ADD PRIMARY KEY (`BrandID`);

--
-- Indexes for table `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`UserID`,`ProductID`),
  ADD KEY `fk_cartProduct` (`ProductID`);

--
-- Indexes for table `detailtransaction`
--
ALTER TABLE `detailtransaction`
  ADD PRIMARY KEY (`TransactionID`,`ProductID`),
  ADD KEY `fk_transProduct` (`ProductID`);

--
-- Indexes for table `headertransaction`
--
ALTER TABLE `headertransaction`
  ADD PRIMARY KEY (`TransactionID`),
  ADD KEY `fk_transUser` (`UserID`);

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`ProductID`),
  ADD KEY `fk` (`BrandID`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`UserID`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `cart`
--
ALTER TABLE `cart`
  ADD CONSTRAINT `fk_cartProduct` FOREIGN KEY (`ProductID`) REFERENCES `product` (`ProductID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_cartUser` FOREIGN KEY (`UserID`) REFERENCES `user` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `detailtransaction`
--
ALTER TABLE `detailtransaction`
  ADD CONSTRAINT `fk_transProduct` FOREIGN KEY (`ProductID`) REFERENCES `product` (`ProductID`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `fk_transTrans` FOREIGN KEY (`TransactionID`) REFERENCES `headertransaction` (`TransactionID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `headertransaction`
--
ALTER TABLE `headertransaction`
  ADD CONSTRAINT `fk_transUser` FOREIGN KEY (`UserID`) REFERENCES `user` (`UserID`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `product`
--
ALTER TABLE `product`
  ADD CONSTRAINT `fk` FOREIGN KEY (`BrandID`) REFERENCES `brand` (`BrandID`) ON DELETE CASCADE ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
