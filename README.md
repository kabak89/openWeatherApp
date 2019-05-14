schema {
  query: Query
  mutation: Mutation
}

scalar Date

type Query {
#  users section
  users: [User]
  userByUsername(username: String!): User

#  books section
  books: [Book]
  book(bookSearch: BookSearchInput!, offset: Int, limit: Int, orderBy: String): [Book]
  booksCount(bookSearch: BookSearchInput!): Long
  bookLanguages: [String]

#  authors section
  authors: [Author]
  author(authorSearch: AuthorSearchInput!): [Author]

#  customers section
  customers: [Customer]
  customer(customerSearch: CustomerSearchInput!): [Customer]
  customerByFIO(fio: String!): [Customer]

#  searchTags section
  searchTags: [SearchTag]
  searchTag(searchTag: SearchTagInput!, offset: Int, limit: Int, orderBy: String): [SearchTag]

#  genre section
  searchGenres: [Genre]
  searchGenre(genreSearch: GenreInput!, offset: Int, limit: Int, orderBy: String): [Genre]

# suppliers section
  suppliers: [Supplier]
  supplier(supplierSearch: SupplierSearchInput!): [Supplier]

# waybills section
  waybills: [Waybill]
  searchWaybills(filter: WaybillFilter!): [Waybill]
  waybillItems(waybillItemSearch: WaybillItemSearchInput!): [WaybillItem]

# editions section
  editions: [Edition]
}

type Mutation {
#  users section
  setUser(userInput: UserInput!): User
  removeUser(username: String!): Boolean

#  suppliers section
  setSupplier(supplierInput: SupplierInput!, supplierId: String = ""): Supplier
  removeSupplier(id: String!): Boolean

#  authors section
  setAuthor(authorInput: AuthorInput!, authorId: String = ""): Author
  removeAuthor(id: String!): Boolean

#  genres section
  setGenre(genre: GenreInput!): Genre
  removeGenre(id: String!): Boolean

  setBook(bookInput: BookInput!): Book
  setCustomer(customerInput: CustomerInput!, customerId: String = ""): Customer
  setPurchase(rfidMark: [String]!, customerId: String): Customer
  setSearchTag(searchTag: SearchTagInput!): SearchTag
  setWaybill(waybill: WaybillInput!): Waybill
}

type Waybill {
  id: ID!
  number: Int!
  date: Date
  supplierId: String!
  supplier: Supplier
  items: [WaybillItem]
  supplierItem: WaybillItem
}

type WaybillItem {
  id: ID
  name: String
  productCode: String
  unitName: String
  unitCode: String
  packageType: String
  amtOnePlaces: Int
  amtPlaces: Int
  grossWeight: Float
  amtWaybill: Int
  amtFactual: Int
  price: Float
  sum: Float
  rateVAT: Float
  sumVAT: Float
  priceIncludingVAT: Float
  sumIncludingVAT: Float
}

input WaybillInput {
  id: String
  number: Int!
  supplierId: String!
  items: [WaybillItemInput]!
}

input WaybillItemInput {
  id: String
  name: String
  productCode: String
  unitName: String
  unitCode: String
  packageType: String
  amtOnePlaces: Int
  amtPlaces: Int
  grossWeight: Float
  amtWaybill: Int
  amtFactual: Int
  price: Float
  sum: Float
  rateVAT: Float
  sumVAT: Float
  priceIncludingVAT: Float
  sumIncludingVAT: Float
}

input WaybillItemSearchInput {
  supplierId: String!
  name: String!
}

type Supplier {
  id: ID!
  name: String!
  markup: Float
}

input SupplierInput {
  id: String
  name: String!
  markup: Float
}

input SupplierSearchInput {
  id: String
  name: String
}

type Book {
  id: ID!
  title: String!
  bookName: String
  series: String
  lang: String
  imageId: [String]
  authors: [Author]
  edition: Edition
  price: Float!
  items: [BookItem]
  tags: [SearchTag]
  genres: [Genre]
}

type BookItem {
  id: ID!
  rfidMark: String!
  bookStatus: BookStatus
  location: RFIDMark
  isbn: String
  year: Int
  waybill: [Waybill]
}

type Edition {
  id: String!
  name: String!
}

input BookInput {
  id: String
  title: String!
  bookName: String
  series: String
  lang: String
  imageId: [String]
  editionId: String
  authorsId: [String]
  price: Float!
  genreIds: [String]
  items: [BookItemInput]
  tagIds: [String]
}

input BookItemInput {
  id: String
  rfidMark: String!
  bookStatus: BookStatus
  isbn: String
  year: Int
}

input BookSearchInput {
  id: String
  title: String
  authorId: String
  editionId: String
  lang: String
  tagIds: [String]
  nameFromSupplier: String
  supplierId: String
  genreIds: [String]
  price: Float
  minPrice: Float
  maxPrice: Float
  bookStatus: String
}

type RFIDMark {
    id: ID
    tag: String
    zone: String
}

type Author {
  id: ID!
  fullName: String!
  role: AuthorRoles
  birthDate: Date
  books: [Book]
  deathDate: Date
  description: String
}

input AuthorInput {
  id: String
  fullName: String!
  role: AuthorRoles
  birthDate: Date
  deathDate: Date
  description: String
  booksId: [String]
}

input AuthorSearchInput {
  id: String
  role: AuthorRoles
}

type User {
  id: ID!
  username: String!
  password: String!
  firstName: String
  secondName: String
  enabled: Boolean
  roles: [UserRoles]!
}

input UserInput {
  username: String!
  password: String!
  firstName: String
  secondName: String
  enabled: Boolean
  roles: [UserRoles]!
}

enum UserRoles {
  ROLE_EMPLOYEE,
  ROLE_ADMIN
}

enum AuthorRoles {
  WRITER,
  PAINTER,
  TRANSLATOR
}

enum BookStatus {
  UNAVAILABLE,
  AVAILABLE,
  LOST,
  IN_USE,
  SOLD
}

type Customer {
  id: ID!
  firstName: String!
  secondName: String
  thirdName: String
  cardId: String
  imageId: String
  birthDate: Date
  purchases: [Purchase]
}

input CustomerInput {
  firstName: String!
  secondName: String
  thirdName: String
  cardId: String
  imageId: String
  birthDate: Date
  purchases: [PurchaseInput]
}

input CustomerSearchInput {
  id: String
  cardId: String
}

type Genre {
  id: ID!
  title: String
  bookIds: [String]
}

input GenreInput {
  id: String
  title: String!
}

type SearchTag {
  id: ID!
  indexName: String
}

input SearchTagInput {
  id: String
  indexName: String
}

input PurchaseInput {
  id: String!
  totalCost: Float!
  datetime: Date!
  items: [PurchaseItemInput]!
}

input PurchaseItemInput {
  id: String!
  bookId: String!
  price: Float!
  title: String!
  imageId: [String]
  authors: [AuthorInput]
}

type Purchase {
  id: ID!
  totalCost: Float!
  datetime: Date!
  items: [PurchaseItem]
}

type PurchaseItem {
  id: ID!
  bookId: String!
  price: Float!
  title: String!
  imageId: [String]
  authors: [Author]
}

input WaybillFilter {
  id: String
}
