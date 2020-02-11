package com.feathermind.matrix.trait

/**
 * hutool不支持trait属性的更新
 * 因为trait生成的field名带了前缀，而HuTools是根据field来copy的，不根据property
 *
 * 以下两个属性不需要显式更新，所以可以用trait
 */
trait AutoTime {
    Date dateCreated
    Date lastUpdated
}
