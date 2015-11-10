-- phpMyAdmin SQL Dump
-- version 4.1.14
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Nov 10, 2015 at 02:06 AM
-- Server version: 5.6.17
-- PHP Version: 5.5.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- Database: `hma`
--

-- --------------------------------------------------------

--
-- Table structure for table `addedapplications`
--

CREATE TABLE IF NOT EXISTS `addedapplications` (
  `appname` varchar(100) NOT NULL,
  `deviceid` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `addedapplications`
--

INSERT INTO `addedapplications` (`appname`, `deviceid`) VALUES
('magic', 'iphone'),
('calculator', 'iphone'),
('voicesearch', 'motox'),
('com.facebook.katana', 'nexus5'),
('com.facebook.katana', 'nexus5'),
('com.facebook.katana', 'nexus5'),
('com.facebook.katana', 'nexus5'),
('com.facebook.katana', 'nexus5'),
('com.facebook.katana', 'nexus5'),
('com.facebook.katana', 'nexus5');

-- --------------------------------------------------------

--
-- Table structure for table `applications`
--

CREATE TABLE IF NOT EXISTS `applications` (
  `deviceid` varchar(100) NOT NULL,
  `appname` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `applications`
--

INSERT INTO `applications` (`deviceid`, `appname`) VALUES
('nexus5', 'google'),
('nexus5', 'gmail'),
('nexus5', 'facebook'),
('nexus5', 'whatsapp'),
('motox', 'yuptv'),
('motox', 'espn'),
('motox', 'skysports'),
('motox', 'espnstar'),
('iphone', 'spotify'),
('iphone', 'pandora'),
('iphone', 'googlemusic'),
('iphone', 'raaga'),
('nexus5', 'lv.n3o.sharkreader'),
('nexus5', 'com.example.android.sunshine.app'),
('nexus5', 'com.google.android.youtube'),
('nexus5', 'com.android.providers.telephony'),
('nexus5', 'com.google.android.gallery3d'),
('nexus5', 'com.google.android.googlequicksearchbox'),
('nexus5', 'com.android.providers.calendar');

-- --------------------------------------------------------

--
-- Table structure for table `deviceinfo`
--

CREATE TABLE IF NOT EXISTS `deviceinfo` (
  `username` varchar(100) NOT NULL,
  `email` varchar(100) NOT NULL,
  `deviceid` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `deviceinfo`
--

INSERT INTO `deviceinfo` (`username`, `email`, `deviceid`) VALUES
('prajit', 'pdas@gmail.com', 'nexus5'),
('prajit', 'pd@gmail.com', 'motox'),
('sudip', 'mittal@gmail.com', 'iphone'),
('devacctpkd', 'devacctpkd@gmail.com', 'nexus5');

-- --------------------------------------------------------

--
-- Table structure for table `removedapplications`
--

CREATE TABLE IF NOT EXISTS `removedapplications` (
  `appname` varchar(100) NOT NULL,
  `deviceid` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `removedapplications`
--

INSERT INTO `removedapplications` (`appname`, `deviceid`) VALUES
('angrybirds', 'nexus5'),
('beautycamera', 'motox');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
