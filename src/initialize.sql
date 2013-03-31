--    This file is part of IMP.

--    Imp is free software: you can redistribute it and/or modify
--    it under the terms of the GNU General Public License as published by
--    the Free Software Foundation, either version 3 of the License, or
--    (at your option) any later version.

--    IMP is distributed in the hope that it will be useful,
--    but WITHOUT ANY WARRANTY; without even the implied warranty of
--    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
--    GNU General Public License for more details.

--    You should have received a copy of the GNU General Public License
--    along with IMP.  If not, see <http://www.gnu.org/licenses/>.

--
-- Definition of table `advertisement_table`
--

CREATE TABLE "advertisement_table" (
  "adId" varchar(255) PRIMARY KEY,
  "title" varchar(255) DEFAULT NULL,
  "projectedStartDate" date DEFAULT NULL,
  "expiryDate" date DEFAULT NULL,
  "institute" varchar(255) DEFAULT NULL,
  "projectType" varchar(255) DEFAULT NULL,
  "targetedAudience" varchar(255) DEFAULT NULL,
  "description" varchar(255),
  "followersVisible" varchar(255) DEFAULT NULL,
  "archivedDate" varchar(255) DEFAULT NULL,
  "Search" boolean DEFAULT NULL,
  "visible" boolean DEFAULT NULL
);


--
-- Definition of table `user_table`
--

CREATE TABLE "user_table" (
  "userId" varchar(255) PRIMARY KEY, 
  "academicTitle" varchar(255) DEFAULT NULL,
  "forename" varchar(255) DEFAULT NULL,
  "surname" varchar(255) DEFAULT NULL,
  "email" varchar(255) DEFAULT NULL,
  "password" varchar(255) DEFAULT NULL,
  "status" varchar(255) DEFAULT NULL
);


--
-- Definition of table `initiator_table`
--
CREATE TABLE "initiator_table" (
  "userId" varchar(255) NOT NULL,
  "adId" varchar(255) NOT NULL,
  "adCreated" date DEFAULT NULL,
  PRIMARY KEY ("userId", "adId"),
  FOREIGN KEY ("userId") REFERENCES "user_table" ("userId"),
  FOREIGN KEY ("adId") REFERENCES "advertisement_table" ("adId")
);


--
-- Definition of table `follower_table`
--

CREATE TABLE "follower_table" (
  "userId" varchar(255) NOT NULL,
  "adId" varchar(255) NOT NULL,
  "visible" boolean DEFAULT NULL,
  "description" varchar(255),
  PRIMARY KEY ("userId", "adId"),
  FOREIGN KEY ("userId") REFERENCES "user_table" ("userId"),
  FOREIGN KEY ("adId") REFERENCES "advertisement_table" ("adId")
);
