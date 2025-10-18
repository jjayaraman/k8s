module "jay_vpc" {
  source = "terraform-aws-modules/vpc/aws"
  version = "4.0.2"

  name = "jay-vpc"
  cidr = "10.0.0.0/16"
  
  azs = ["eu-west-2a", "eu-west-2b", ]
  public_subnets = ["10.0.101.0/24","10.0.102.0/24"]
  # private_subnets = ["10.0.1.0/24","10.0.2.0/24"]

  # enable_nat_gateway = true
  # single_nat_gateway = true     
  enable_dns_hostnames = true

  tags = {
    Terraform   = "true"
    Environment = "dev"
  }  
}


