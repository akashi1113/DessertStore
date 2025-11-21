-- product-service.sql
-- 商品服务独立数据库

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Database structure for product-service
-- ----------------------------
CREATE DATABASE IF NOT EXISTS `product-service` DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
USE `product-service`;

-- ----------------------------
-- Table structure for category
-- ----------------------------
DROP TABLE IF EXISTS `category`;
CREATE TABLE `category`  (
                             `catid` varchar(20) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
                             `name` varchar(80) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
                             `image` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
                             PRIMARY KEY (`catid`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of category
-- ----------------------------
INSERT INTO `category` VALUES ('AMERICAN', 'American', 'https://feshin.oss-cn-beijing.aliyuncs.com/Mooncake.jpg');
INSERT INTO `category` VALUES ('CHINESE', 'Chinese', 'https://feshin.oss-cn-beijing.aliyuncs.com/Cinnamon%20Roll.jpg');
INSERT INTO `category` VALUES ('EUROPEAN', 'European', 'https://feshin.oss-cn-beijing.aliyuncs.com/Rye%20Bread.jpg');
INSERT INTO `category` VALUES ('JAPANESE', 'Japanese', 'https://feshin.oss-cn-beijing.aliyuncs.com/square%20toast.jpg');

-- ----------------------------
-- Table structure for product
-- ----------------------------
DROP TABLE IF EXISTS `product`;
CREATE TABLE `product`  (
                            `productid` varchar(50) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
                            `name` varchar(80) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
                            `category` varchar(80) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
                            `image` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
                            PRIMARY KEY (`productid`) USING BTREE,
                            INDEX `productCat`(`name` ASC) USING BTREE,
                            INDEX `productName`(`category` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of product
-- ----------------------------
INSERT INTO `product` VALUES ('AB-01', 'Soft Bread', 'AMERICAN', 'https://feshin.oss-cn-beijing.aliyuncs.com/White%20Toast.jpg');
INSERT INTO `product` VALUES ('AB-02', 'Specialty Bread', 'AMERICAN', 'https://feshin.oss-cn-beijing.aliyuncs.com/Bagel.jpg');
INSERT INTO `product` VALUES ('AB-03', 'Sweet Bread', 'AMERICAN', 'https://feshin.oss-cn-beijing.aliyuncs.com/Banana%20Bread.jpg');
INSERT INTO `product` VALUES ('CB-01', 'Festival Snack', 'CHINESE', 'https://feshin.oss-cn-beijing.aliyuncs.com/Mooncake.jpg');
INSERT INTO `product` VALUES ('CB-02', 'Regional Bread', 'CHINESE', 'https://feshin.oss-cn-beijing.aliyuncs.com/Hong%20Kong-style%20Cocktail%20Bun.jpg');
INSERT INTO `product` VALUES ('CB-03', 'Traditional Bread', 'CHINESE', 'https://feshin.oss-cn-beijing.aliyuncs.com/Custard%20Bun.jpg');
INSERT INTO `product` VALUES ('EB-01', 'French Bread', 'EUROPEAN', 'https://feshin.oss-cn-beijing.aliyuncs.com/Baguette.jpg');
INSERT INTO `product` VALUES ('EB-02', 'German Bread', 'EUROPEAN', 'https://feshin.oss-cn-beijing.aliyuncs.com/Pretzel.jpg');
INSERT INTO `product` VALUES ('EB-03', 'Italian Bread', 'EUROPEAN', 'https://feshin.oss-cn-beijing.aliyuncs.com/Focaccia.jpg');
INSERT INTO `product` VALUES ('JB-01', 'Filled Bread', 'JAPANESE', 'https://feshin.oss-cn-beijing.aliyuncs.com/Yakisoba%20Pan.jpg');
INSERT INTO `product` VALUES ('JB-02', 'Seasoned Bread', 'JAPANESE', 'https://feshin.oss-cn-beijing.aliyuncs.com/Melonpan.jpg');
INSERT INTO `product` VALUES ('JB-03', 'Toast', 'JAPANESE', 'https://feshin.oss-cn-beijing.aliyuncs.com/Nama%20Toast.jpg');

-- ----------------------------
-- Table structure for item
-- ----------------------------
DROP TABLE IF EXISTS `item`;
CREATE TABLE `item`  (
                         `name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
                         `itemid` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
                         `productid` varchar(10) CHARACTER SET utf8 COLLATE utf8_bin NOT NULL,
                         `price` decimal(10, 2) NULL DEFAULT NULL,
                         `supplier` int NULL DEFAULT NULL,
                         `status` varchar(2) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
                         `descn` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
                         `image` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin NULL DEFAULT NULL,
                         `stock` int NULL DEFAULT NULL,
                         PRIMARY KEY (`itemid`) USING BTREE,
                         INDEX `fk_item_2`(`supplier` ASC) USING BTREE,
                         INDEX `itemProd`(`productid` ASC) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8 COLLATE = utf8_bin ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of item
-- ----------------------------
INSERT INTO `item` VALUES ('White Toast', 'EST-1', 'AB-01', 10.00, 1, 'P', 'With a dense texture, it is often used for making sandwiches.', 'https://feshin.oss-cn-beijing.aliyuncs.com/White%20Toast.jpg', 2);
INSERT INTO `item` VALUES ('Whole Wheat Bread', 'EST-100', 'AB-01', 8.00, 1, 'P', 'A healthier version with added whole wheat flour.', 'https://feshin.oss-cn-beijing.aliyuncs.com/Whole%20Wheat%20Bread.jpg', 3);
INSERT INTO `item` VALUES ('Bagel', 'EST-101', 'AB-02', 12.00, 1, 'P', 'Boiled and then baked, it is a symbol of New York with a crispy exterior and chewy interior.', 'https://feshin.oss-cn-beijing.aliyuncs.com/Bagel.jpg', 2);
INSERT INTO `item` VALUES ('Cornbread', 'EST-102', 'AB-02', 12.00, 1, 'P', 'Made from coarse cornmeal, it is a traditional food in the American South.', 'https://feshin.oss-cn-beijing.aliyuncs.com/Cornbread.jpg', 4);
INSERT INTO `item` VALUES ('Banana Bread', 'EST-103', 'AB-03', 15.00, 1, 'P', 'A quick bread that uses mashed bananas to increase its moisture.', 'https://feshin.oss-cn-beijing.aliyuncs.com/Banana%20Bread.jpg', 3);
INSERT INTO `item` VALUES ('Cinnamon Roll', 'EST-104', 'AB-03', 12.00, 1, 'P', 'A classic combination of the aroma of icing sugar and cinnamon.', 'https://feshin.oss-cn-beijing.aliyuncs.com/Cinnamon%20Roll.jpg', 3);
INSERT INTO `item` VALUES ('Char Siu Bao', 'EST-105', 'CB-03', 5.00, 1, 'P', 'Western-style dough filled with Cantonese-style char siu filling.', 'https://feshin.oss-cn-beijing.aliyuncs.com/Char%20Siu%20Bao.jpg', 3);
INSERT INTO `item` VALUES ('Custard Bun', 'EST-106', 'CB-03', 5.00, 1, 'P', 'A soft bread body with a runny custard filling.', 'https://feshin.oss-cn-beijing.aliyuncs.com/Custard%20Bun.jpg', 5);
INSERT INTO `item` VALUES ('Hong Kong-style Cocktail Bun', 'EST-107', 'CB-02', 10.00, 1, 'P', 'A nostalgic flavor of shredded coconut filling and bread rolls.', 'https://feshin.oss-cn-beijing.aliyuncs.com/Hong%20Kong-style%20Cocktail%20Bun.jpg', 4);
INSERT INTO `item` VALUES ('Taiwanese Pineapple Bun', 'EST-108', 'CB-02', 10.00, 1, 'P', 'A combination of cracked crumbs and hot and cold butter.', 'https://feshin.oss-cn-beijing.aliyuncs.com/Taiwanese%20Pineapple%20Bun.jpg', 4);
INSERT INTO `item` VALUES ('Mochi Bread', 'EST-109', 'CB-01', 2.00, 1, 'P', 'An internet-famous product with a chewy mochi filling.', 'https://feshin.oss-cn-beijing.aliyuncs.com/Mochi%20Bread.jpg', 3);
INSERT INTO `item` VALUES ('Mooncake', 'EST-110', 'CB-02', 8.00, 1, 'P', 'A crust made from inverted syrup with innovative fillings.', 'https://feshin.oss-cn-beijing.aliyuncs.com/Mooncake.jpg', 3);
INSERT INTO `item` VALUES ('Baguette', 'EST-111', 'EB-01', 14.00, 1, 'P', 'The cracked crust is a sign of its quality.', 'https://feshin.oss-cn-beijing.aliyuncs.com/Baguette.jpg', 2);
INSERT INTO `item` VALUES ('Croissant', 'EST-14', 'EB-01', 12.00, 1, 'P', 'A representative of the Viennese technique of laminating with butter.', 'https://feshin.oss-cn-beijing.aliyuncs.com/Croissant.jpg', 3);
INSERT INTO `item` VALUES ('Pretzel', 'EST-15', 'EB-02', 10.00, 1, 'P', 'With a dark brown crispy crust, it is a great companion for beer.', 'https://feshin.oss-cn-beijing.aliyuncs.com/Pretzel.jpg', 2);
INSERT INTO `item` VALUES ('Rye Bread', 'EST-16', 'EB-02', 15.00, 1, 'P', 'With a rustic flavor from the sourdough and rye.', 'https://feshin.oss-cn-beijing.aliyuncs.com/Rye%20Bread.jpg', 1);
INSERT INTO `item` VALUES ('Focaccia', 'EST-17', 'EB-03', 13.00, 1, 'P', 'A flat bread with a herbal flavor soaked in olive oil.', 'https://feshin.oss-cn-beijing.aliyuncs.com/Focaccia.jpg', 5);
INSERT INTO `item` VALUES ('Panettone', 'EST-18', 'EB-03', 15.00, 1, 'P', 'A dried fruit sweet bread exclusive for Christmas.', 'https://feshin.oss-cn-beijing.aliyuncs.com/Panettone.jpg', 6);
INSERT INTO `item` VALUES ('Red Bean Bread', 'EST-19', 'JB-01', 18.00, 1, 'P', 'A national bread that originated in the Meiji period.', 'https://feshin.oss-cn-beijing.aliyuncs.com/Red%20Bean%20Bread.jpg', 4);
INSERT INTO `item` VALUES ('Yakisoba Pan', 'EST-2', 'JB-01', 10.00, 1, 'P', 'A popular choice among students with a combination of sweet and savory flavors.', 'https://feshin.oss-cn-beijing.aliyuncs.com/Yakisoba%20Pan.jpg', 5);
INSERT INTO `item` VALUES ('Curry Bread', 'EST-20', 'JB-02', 8.00, 1, 'P', 'A deep-fried bread filled with Japanese-style curry filling.', 'https://feshin.oss-cn-beijing.aliyuncs.com/Curry%20Bread.jpg', 5);
INSERT INTO `item` VALUES ('Melonpan', 'EST-21', 'JB-02', 10.00, 1, 'P', ' A sweet dough covered with cookie crumbs.', 'https://feshin.oss-cn-beijing.aliyuncs.com/Melonpan.jpg', 5);
INSERT INTO `item` VALUES ('Nama Toast', 'EST-22', 'JB-03', 10.00, 1, 'P', 'A premium version without fillings but with a rich milky aroma.', 'https://feshin.oss-cn-beijing.aliyuncs.com/Nama%20Toast.jpg', 5);
INSERT INTO `item` VALUES ('square toast', 'EST-23', 'JB-03', 20.00, 1, 'P', 'A demonstration of craftsmanship with an extremely stretchy effect.', 'https://feshin.oss-cn-beijing.aliyuncs.com/square%20toast.jpg', 5);

-- ----------------------------
-- Table structure for review
-- ----------------------------
DROP TABLE IF EXISTS `review`;
CREATE TABLE `review`  (
                           `userid` bigint NOT NULL,
                           `itemid` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL,
                           `score` int NOT NULL,
                           `comment` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_0900_ai_ci ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Records of review
-- ----------------------------

SET FOREIGN_KEY_CHECKS = 1;