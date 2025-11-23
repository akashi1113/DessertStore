/*
 Navicat Premium Dump SQL

 Source Server         : mysql8
 Source Server Type    : MySQL
 Source Server Version : 80404 (8.4.4)
 Source Host           : localhost:3306
 Source Schema         : bakery-order

 Target Server Type    : MySQL
 Target Server Version : 80404 (8.4.4)
 File Encoding         : 65001

 Date: 24/11/2025 01:36:38
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for cart
-- ----------------------------
DROP TABLE IF EXISTS `cart`;
CREATE TABLE `cart`  (
  `cartid` bigint NOT NULL AUTO_INCREMENT,
  `subTotal` double NULL DEFAULT NULL,
  `totalQuantity` double NULL DEFAULT NULL,
  `userid` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`cartid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 32 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cart
-- ----------------------------
INSERT INTO `cart` VALUES (28, 46, 5, 27);
INSERT INTO `cart` VALUES (29, 58, 6, 29);
INSERT INTO `cart` VALUES (30, 10, 1, 36);
INSERT INTO `cart` VALUES (31, 54, 6, 37);

-- ----------------------------
-- Table structure for cartitem
-- ----------------------------
DROP TABLE IF EXISTS `cartitem`;
CREATE TABLE `cartitem`  (
  `itemid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `cartid` bigint NULL DEFAULT NULL,
  `quantity` int NULL DEFAULT NULL,
  `totalPrice` double NULL DEFAULT NULL,
  `price` double NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of cartitem
-- ----------------------------
INSERT INTO `cartitem` VALUES ('EST-107', 29, 2, 20, 10);
INSERT INTO `cartitem` VALUES ('EST-107', 30, 1, 10, 10);
INSERT INTO `cartitem` VALUES ('EST-107', 31, 1, 10, 10);
INSERT INTO `cartitem` VALUES ('EST-110', 31, 3, 24, 8);
INSERT INTO `cartitem` VALUES ('EST-1', 31, 2, 20, 10);
INSERT INTO `cartitem` VALUES ('EST-107', 28, 3, 30, 10);
INSERT INTO `cartitem` VALUES ('EST-110', 28, 2, 16, 8);
INSERT INTO `cartitem` VALUES ('EST-108', 29, 3, 30, 10);
INSERT INTO `cartitem` VALUES ('EST-110', 29, 1, 8, 8);

-- ----------------------------
-- Table structure for lineitem
-- ----------------------------
DROP TABLE IF EXISTS `lineitem`;
CREATE TABLE `lineitem`  (
  `orderid` bigint NULL DEFAULT NULL,
  `itemid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `cartid` bigint NULL DEFAULT NULL,
  `quantity` int NULL DEFAULT NULL,
  `totalPrice` double NULL DEFAULT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of lineitem
-- ----------------------------
INSERT INTO `lineitem` VALUES (1, 'AM-1', 1, 2123, 1233);
INSERT INTO `lineitem` VALUES (2, '89', 79, 11, 370.95);
INSERT INTO `lineitem` VALUES (3, '89', 79, 11, 370.95);
INSERT INTO `lineitem` VALUES (4, '9', 79, 11, 370.95);
INSERT INTO `lineitem` VALUES (5, 'AM-1', 27, 32, 324);
INSERT INTO `lineitem` VALUES (6, 'AM-1', 27, 32, 324);
INSERT INTO `lineitem` VALUES (7, 'EST-109', 29, 23, 46);
INSERT INTO `lineitem` VALUES (8, 'EST-107', 29, 1, 10);
INSERT INTO `lineitem` VALUES (16, 'EST-107', 30, 1, 10);
INSERT INTO `lineitem` VALUES (5, 'EST-17', 5, 1, 13);

-- ----------------------------
-- Table structure for order
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order`  (
  `orderid` bigint NOT NULL AUTO_INCREMENT,
  `orderdate` date NULL DEFAULT NULL,
  `shippingCost` double NULL DEFAULT NULL,
  `tax` double NULL DEFAULT NULL,
  `grandTotal` double NULL DEFAULT NULL,
  `shippingAddress1` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `shippingAddress2` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `firstname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `lastname` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `city` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `phone` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NULL DEFAULT NULL,
  `subTotal` double NULL DEFAULT NULL,
  `userid` bigint NULL DEFAULT NULL,
  PRIMARY KEY (`orderid`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 6 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES (1, '2020-03-01', 123, 1, 1, 'ds', 'da', 'w', NULL, NULL, NULL, NULL, 29);
INSERT INTO `order` VALUES (2, '2025-04-05', NULL, NULL, 74, '吉林省 成海市 乐平市 羊巷9号 83号院', '北京市 西门市 徽州区 哈中心9132号 70号门牌', '振家', '庄', '海州市', '60417752159', NULL, 29);
INSERT INTO `order` VALUES (3, '2025-04-05', NULL, NULL, 74, '吉林省 成海市 乐平市 羊巷9号 83号院', '北京市 西门市 徽州区 哈中心9132号 70号门牌', '振家', '庄', '海州市', '60417752159', NULL, 1);
INSERT INTO `order` VALUES (4, '2025-04-05', NULL, NULL, 74, '12ds', 'des', 'we', 'J', 'cd', '6042159', NULL, 1);
INSERT INTO `order` VALUES (5, '2025-11-24', NULL, NULL, 36, '福建省 武林市 峨山彝族自治县 位旁98号 27室', '黑龙江省 太海市 普宁市 赖侬8823号 84单元', '瑾瑜', '茆', '南海市', '027 4835 8195', NULL, 27);

SET FOREIGN_KEY_CHECKS = 1;
